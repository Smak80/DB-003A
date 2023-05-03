import ru.smak.database.Customer;
import ru.smak.database.DbHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            DbHelper dbh = new DbHelper("localhost", 3306, "root", "");
            dbh.createDb();
            dbh.createDbStructure();
            var newUsers = new ArrayList<Customer>();
            newUsers.add(new Customer("+79271110500", "Иванов И. И.", "123456"));
            newUsers.add(new Customer("+79170000890", "Петров П. П.", "qwerty"));
            newUsers.add(new Customer("+79991110001", "Сидорова С. С.", "skdjfh"));
            newUsers.add(new Customer("+79271110511", "Иванова Е. И.", "654321"));
            for (var u: newUsers){
                if (!dbh.addUser(u)){
                    System.out.println("Пользователь с номером "+u.getPhone()+" уже зарегистрирован");
                }
            }
            var users = dbh.getUsers("Иван");
            for (var u: users){
                System.out.println(u);
                try {
                    System.out.println(u.verifyPassword("123456", u.getPassword()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка: "+e.getMessage());
        }
    }
}
