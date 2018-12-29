package hugo.weaving.log;

/**
 * Created by lixindong2 on 12/29/18.
 */

public interface Filter<T> {
    boolean filter(T t);
}
