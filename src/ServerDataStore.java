import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


public class ServerDataStore implements DataStore {
    private final Map<Integer,String> store = new HashMap<>();

    @Override
    public void write(int index, String data) {
        store.put(index, data);
    }

    @Override
    public String read(int index) throws NoSuchElementException {
        if(store.containsKey(index))
            return store.get(index);
        throw new NoSuchElementException("No value at index " + index);
    }
}
