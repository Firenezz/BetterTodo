package todolist.utils;

import net.minecraft.util.Tuple;

@SuppressWarnings("unchecked")
public class GenericTuple<T1, T2> extends Tuple {

    public GenericTuple(T1 first, T2 second) {
        super(first, second);
    }

    @Override
    public T1 getFirst() {
        return (T1) super.getFirst();
    }

    @Override
    public T2 getSecond() {
        return (T2) super.getSecond();
    }
}
