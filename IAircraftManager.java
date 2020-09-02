import java.util.*;

public interface IAircraftManager {

    public void list();
    public List<Aircraft> getAll();
    public void findAir(String reg_No);
    public Aircraft find(String reg_No);
    public void removeAir(String reg_No);
    public void update(String reg_No, String type, String name, int capacity);
     public boolean create(String reg_No, String type, String name, int capacity);

}