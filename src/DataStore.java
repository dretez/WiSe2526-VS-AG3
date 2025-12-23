import java.util.NoSuchElementException;

public interface DataStore {
    public void write(int index, String data);
    public String read(int index) throws NoSuchElementException;
}
