package service;

import model.Action;
import model.ActionState;
import model.Event;
import model.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LogServiceImpl implements LogService {

    private UserService userService;
    private Map<User, Map<String, List<Event>>> mainLog = new HashMap<>();

    public LogServiceImpl (UserService userService ){
        this.userService = userService;
    }

    @Override
    public Map<User, Map<String, List<Event>>> prepareStructure() {
        List<User> userList = userService.getUserList(); //получили список пользователей
        Scanner log = readFileLog(); // прочитали лог сканером
        log.useDelimiter(System.lineSeparator()); //разделили по строкам

        while (log.hasNext()) { //нашли event в логе (первая строка) и идем, пока строки не кончатся
            String event = log.nextLine(); // записали в строку
            String[] data = event.split(" "); //разделили строку event'a пробелами, записали в массив data

            User user = userService.getUserByName(data[1]); //Сопоставили пользователя, по записи второго элемента массива data
            Event newEvent = createEvent(data); //создали событие из данных массива data
            String date = data[2]; //сохранили дату события
            if (!(user == null)){ // проверяем, не попался ли в логе юзер не из списка юзеров
                addNewUserOrEventToMainLog(date, newEvent, user);
            }
              // проверяем, если user уже есть в mainLog
            // то дальше (если такая дата есть) добавляем событие в лист событий по дате,
            // либо (если даты нет) - создаем новую дату и новый лист событий для этой даты (сразу с событием)
            // если пользователя еще нет - создаем запись пользователя, даты, листа событий с событием
        }
        return mainLog;
    }


    @Override
    public Event createEvent(String[] data) {
        return new Event(
                data[0], // ip
                data[2], // дата
                data[3], // адрес
                findAction(data[4]), // действие
                findActionState(data[5]) //результат действия
        );
    }

    @Override
    public Action findAction(String action) {
        for (Action value : Action.values()) {
            if (value.name().equals(action)){
                return value;
            }
        }
        return null;
    }

    @Override
    public ActionState findActionState(String actionState) {
        for (ActionState value : ActionState.values()) {
            if (value.name().equals(actionState)){
                return value;
            }
        }
        return null;
    }

    @Override
    public Scanner readFileLog () {
        File file = new File("/Users/zhvalevalex/Documents/telran/parser/src/resources/logfile.txt");
        Scanner scanner = null;
        try {
            return scanner = new Scanner(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addNewUserOrEventToMainLog (String date, Event event, User user) {
        if (mainLog.containsKey(user)) { // если пользователь уже есть в мапе
            Map <String, List<Event>> userLog = mainLog.get(user); // берем value (то есть мапу дата-лист событий) по ключу (пользователь)
            createNewDateOrEventRecord(date,event,userLog); //дописываем в лист или создаем новую дату
        } else {
            createNewUserRecord(user,date,event); // если такого пользователя в мапе нет, создаем запись заново
        }
    }

    @Override
    public void createNewDateOrEventRecord (String date, Event event, Map<String, List<Event>> userLog){
        if (userLog.containsKey(date)) { // если дата уже есть в мапе
            List<Event> events = userLog.get(date); // берем value (то есть лист событий) по ключу (дата)
            events.add(event); // добавляем в лист новое событие (не кладем обратно, т.к. уже изменили объект)
        } else {
            userLog.put(date, new ArrayList<>(List.of(event))); // если мапа пустая или не нашлось такой даты - просто добавляем заново
        }
    }

    @Override
    public void createNewUserRecord (User user, String date, Event event){
        Map <String, List <Event>> userLog = new HashMap<>(); //создаем новую мапу userLog (дата  - лист событий)
        userLog.put(date, new ArrayList<>(List.of(event))); // добавляем в нее дату и новый лист (сразу с событием)
        mainLog.put(user,userLog); //добавляем в основную мапу mainLog (юзер, мапа (дата, лист событий))
    }

    @Override
    public void printAllUsersLogs(){
        List<User> userList = userService.getUserList();
        for (User user : userList) {
            System.out.println("Пользователь :"+user);
            Map <String,List<Event>> userLog = mainLog.get(user);
            for (Map.Entry<String,List<Event>> pair : userLog.entrySet()){
                String date = pair.getKey();
                List <Event> events = pair.getValue();
                System.out.println("Дата: "+date+" Действия: "+events);
            }
            System.out.println("<-------------> ");
        }
    }

}
