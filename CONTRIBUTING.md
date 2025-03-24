

# Contribution

## Environment setup

1. Clone the repository:
   ```
   git clone https://github.com/Firenezz/TodoList.git
   ```

2. Install Java (version 8, 17, or 21) and an IDE of your choice.

3. Navigate to the project directory:
   ```
   cd TodoList
   ```

### Building the Project

1. Update the build script:
   `
   ./gradlew updateBuildScript
   `

2. If this is the first time setting up of your workspace, make sure to run:

   2.1 `./gradlew updateDependencies`

   2.2 `./gradlew setupDecomp`

   2.3 `./gradlew setupDecompWorkspace`

3. Build the project:
   `
   ./gradlew build --build-cache
   `
   The `--build-cache` flag speeds up builds by caching compiled files
   [Documentation](https://docs.gradle.org/current/userguide/build_cache.html).

### Troubleshooting

If the build fails, check the console output for error messages. Common issues include:

- Outdated dependencies
- Issues with Spotless plugin

To resolve these:
- Run `./gradlew updateDependencies` to update dependencies
- Run `./gradlew spotlessApply` to apply Spotless changes

### Contributing Guidelines

1. Fork the repository to your personal GitHub account.

2. Create a new branch for your feature or bug fix:
   ```
   git checkout -b your-feature-name
   ```

3. Make your changes and commit them:
   ```
   git add .
   git commit -m "Description of your changes"
   ```

6. Test your changes thoroughly.

4. Push your branch to GitHub:
   ```
   git push origin your-feature-name
   ```

5. Open a pull request against master branch and provide a description on what it changes/adds/removes.
   Include pictures/graphs if necessary. If there is a related issue, link it.

6. Address review problems. Resolve merge conflicts. Wait for the final merge.

### Run the mod
Run `./gradlew runClient` or `./gradlew runClient17` or `./gradlew runClient21` to run the client

Run `./gradlew runServer` or `./gradlew runServer17` or `./gradlew runServer21` to run the server

### Tips
- Use double shift to find classes if you use IDEA Intellij

### Best Practices

- Write clear, descriptive commit messages
- Follow the existing coding style
- Document any API changes or new functionality

By following these steps and guidelines, you'll be able to effectively contribute to the TodoList project. Remember to
communicate with other contributors and maintainers throughout the process.
