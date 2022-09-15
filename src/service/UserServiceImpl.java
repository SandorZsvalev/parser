package service;

import model.Event;
import model.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class UserServiceImpl implements UserService {

    private List <User> userList;

    public UserServiceImpl () {
        this.userList = new ArrayList<>();
        File file = new File("/Users/zhvalevalex/Documents/telran/parser/src/resources/users.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(System.lineSeparator());
        while (scanner.hasNext()){
            String userName = scanner.nextLine();
            userList.add(new User(userName));
        }
    }

    public List<User> getUserList() {
        return userList;
    }

    @Override
    public User getUserByName(String name) {
        for (User user : userList) {
            if(name.equals(user.getUserName())){
                return user;
            }
        }
        return null;
    }

    @Override
    public void search (Map<User, Map<String, List<Event>>> mainLog){
        String choice = menu();
        while (!choice.equals("0")){
            switch (choice){
                case "1" : {
                    findRecordUserByName(mainLog);
                    choice = menu();
                    break;
                }
                case "2" : {
                    findRecordUserByNameAndDate(mainLog);
                    choice = menu();
                    break;
                }
                case "3" : {
                    findRecordAllUsersByDate(mainLog);
                    choice = menu();
                    break;
                }
                case "4" : {
                    findRecordAllUsersByDateAndSortResult(mainLog);
                    choice = menu();
                    break;
                }
                default: {
                    System.out.println("Введено некорректное значение");
                    choice = menu();
                    break;
                }
            }
        }
        System.exit(0);
    }

    private String menu (){
        System.out.println("\n <--- Система поиска по логу --->");
        System.out.println("по Имени пользователя - 1, " +
                "по Имени и Дате - 2, " +
                "все записи по Дате - 3, " +
                "все записи по Дате (вывод отсортирован по имени пользователя) - 4, " +
                "Выход - 0");
        return input();
    }

    @Override
    public void findRecordUserByName(Map<User, Map<String, List<Event>>> mainLog) {
        System.out.println("Поиск пользователя по Имени");
        System.out.println("Имя: ");
        String name = input();
        User user = getUserByName(name);
        if (mainLog.containsKey(user)){
            System.out.println("Пользователь "+user.getUserName()+" найден");
            Map <String, List <Event>> userLog = mainLog.get(user);
            for (Map.Entry<String, List <Event>> pair : userLog.entrySet()){
                System.out.println("Записи этого пользователя : ");
                String date = pair.getKey();
                List <Event> events = pair.getValue();
                System.out.println("Дата : "+date);
                System.out.println("События : "+events);
            }
        } else {
            System.out.println("Пользователь с таким именем ("+name+") не найден");
        }


    }

    @Override
    public void findRecordUserByNameAndDate(Map<User, Map<String, List<Event>>> mainLog) {
        System.out.println("Поиск записей по Имени пользователя и Дате\n");
        System.out.println("Имя пользователя: ");
        String name = input();
        System.out.println("Дата: ");
        String date = input();
        User user = getUserByName(name);
        if (mainLog.containsKey(user)) {
            System.out.println("Пользователь " + user.getUserName() + " найден");
            Map<String, List<Event>> userLog = mainLog.get(user);
            if (userLog.containsKey(date)) {
                List<Event> events = userLog.get(date);
                System.out.println("Дата : " + date);
                System.out.println("События : " + events);
            } else {
                System.out.println("Событий на эту дату(" + date + ") не найдено");
            }
        } else {
            System.out.println("Пользователь с таким именем (" + name + ") не найден");
        }
    }

    @Override
    public void findRecordAllUsersByDate(Map<User, Map<String, List<Event>>> mainLog) {
        System.out.println("Поиск всех записей по Дате");
        Map <User, List <Event>> allUsersEventsByDate = new HashMap<>();
        System.out.println("Дата: ");
        String date = input();
        for (Map.Entry<User, Map<String, List <Event>>> pair : mainLog.entrySet()){
            User user = pair.getKey();
            Map <String, List <Event>> userLog = pair.getValue();
            if (userLog.containsKey(date)){
                List <Event> events = userLog.get(date);
                allUsersEventsByDate.put(user,events);
            }
        }
        if (!allUsersEventsByDate.isEmpty()){
            for (Map.Entry<User, List<Event>> pair : allUsersEventsByDate.entrySet()){
                User user = pair.getKey();
                List <Event> eventsUser = pair.getValue();
                System.out.println("Пользователь: "+user.getUserName());
                System.out.println("Событие: "+eventsUser);
            }
        } else {
            System.out.println("Записей на указанную дату "+date+" не найдено");
        }
    }

    @Override
    public void findRecordAllUsersByDateAndSortResult (Map<User, Map<String, List<Event>>> mainLog) {
        System.out.println("Поиск всех записей по Дате");
        Map <User, List <Event>> allUsersEventsByDate = new HashMap<>();
        System.out.println("Дата: ");
        String date = input();
        for (Map.Entry<User, Map<String, List <Event>>> pair : mainLog.entrySet()){
            User user = pair.getKey();
            Map <String, List <Event>> userLog = pair.getValue();
            if (userLog.containsKey(date)){
                List <Event> events = userLog.get(date);
                allUsersEventsByDate.put(user,events);
            }
        }
        sortUserByNameAndPrint(allUsersEventsByDate);
    }

    private String input (){
        Scanner scn = new Scanner(System.in);
        System.out.println("Введите строковое значение");
        return scn.nextLine();
    }

    private void sortUserByNameAndPrint (Map <User, List <Event>> allUsersEventsByDate){
        if (!allUsersEventsByDate.isEmpty()){ //если мапа не пустая
            List <User> usersSortedList = new ArrayList<>(); //создали лист пользователей для сортировки
            for (Map.Entry<User, List<Event>> pair : allUsersEventsByDate.entrySet()){ // взяли из мапы
                User user = pair.getKey(); // пользователей
                usersSortedList.add(user); // и добавили их в созданный лист
            }
/*            Comparator <User> comparator = new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getUserName().compareTo(o2.getUserName());
                }
            };*/
            usersSortedList.sort(Comparator.comparing(User::getUserName)); //отсортировали пользователей в листе компаратором по имени
            for (User user : usersSortedList) { //пошли по отсортированному листу
                List <Event> eventsUser = allUsersEventsByDate.get(user); //взли лист событий соответствующий пользователю
                System.out.println("Пользователь: "+user.getUserName());
                System.out.println("Событие: "+eventsUser);
            }
        } else {
            System.out.println("Записей на указанную дату не найдено"); //если мапа пустая, значит событий на дату нет
        }

    }


}
