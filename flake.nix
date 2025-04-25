{
  description = "GTNH TODO Project Development Environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
    treefmt-nix.url = "github:numtide/treefmt-nix";
    flake-parts.url = "github:hercules-ci/flake-parts";
    flake-parts.inputs.nixpkgs.follows = "nixpkgs";

    systems-default = { url = "github:nix-systems/default"; flake = false; };
  };

  outputs = { self, nixpkgs, flake-utils, flake-parts, treefmt-nix, systems-default } @ inputs:
    flake-parts.lib.mkFlake { inherit inputs; } {
      imports = [
        inputs.treefmt-nix.flakeModule
      ];

      systems = import systems-default;

      perSystem = { config, lib, pkgs, ... }:
        let
          # Define Java versions
          jdk8 = pkgs.jdk8;
          jdk17 = pkgs.jdk17;
          jdk21 = pkgs.jdk21;

          # Gradle version from wrapper
          gradleVersion = "8.13";
          gradleDist = pkgs.fetchurl {
            url = "https://services.gradle.org/distributions/gradle-${gradleVersion}-bin.zip";
            sha256 = "sha256-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="; # This will be replaced by nix
          };

          # Common development tools
          devTools = with pkgs; [
            # Version control
            git

            # Build tools
            gradle

            # Optional: Useful development tools
            jq
            curl
          ];

          # Development shell with all Java versions
          devShell = pkgs.mkShell {
            buildInputs = devTools;

            # Make all Java versions available
            packages = [ jdk8 jdk17 jdk21 ];

            # Set JAVA_HOME to JDK 17 by default
            JAVA_HOME = "${jdk17}";

            # Add Java versions to PATH
            shellHook = ''
              export PATH="${jdk8}/bin:${jdk17}/bin:${jdk21}/bin:$PATH"
              echo "Available Java versions:"
              echo "  - Java 8: ${jdk8}"
              echo "  - Java 17: ${jdk17}"
              echo "  - Java 21: ${jdk21}"
              echo "Using Java 17 by default (JAVA_HOME=${jdk17})"
              echo "Use ./gradlew to build the project"
            '';
          };

          # Build derivation with proper Gradle wrapper handling
          buildDerivation = pkgs.stdenv.mkDerivation {
            name = "gtnh-todo";
            src = ./.;

            buildInputs = [ jdk17 pkgs.unzip ];

            # Set up Gradle wrapper
            preBuild = ''
              # Create Gradle wrapper directory
              mkdir -p gradle/wrapper
              # Extract Gradle distribution
              unzip -q ${gradleDist} -d gradle/wrapper/
              mv gradle/wrapper/gradle-${gradleVersion} gradle/wrapper/gradle
              # Make gradlew executable
              chmod +x ./gradlew
            '';

            buildPhase = ''
              export JAVA_HOME=${jdk17}
              ./gradlew --offline build
            '';

            installPhase = ''
              mkdir -p $out
              cp -r build/libs/* $out/
            '';
          };
        in
        {
          devShells.default = devShell;
          treefmt.config = {
            projectRootFile = "flake.nix";
            programs = {
              nixpkgs-fmt.enable = true;
            };
          };
          packages.default = buildDerivation;
        };
    };
}
