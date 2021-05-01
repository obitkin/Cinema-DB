import data.CreationQuery;
import data.Data;
import providers.fields.FieldIntEnum;
import providers.fields.FieldIntGap;
import providers.fields.FieldStringLimited;
import providers.fields.FieldStringRandom;
import tables.Table;

import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyPair;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.sql.Time;

public class Main implements CreationQuery, Data {

    static Random random = new Random();

    static int[] AGE_RATING = new int[] {0, 6, 12, 16, 18, 21};
    static int HALL_ID_MAX = 20;
    static int FILM_ID_MAX = 1000;
    static int GENRES_ID_MAX = 5;
    static int AD_ID_MAX = 50;
    static int NEWSREEL_ID_MAX = 1000;
    static int HALL_TYPE_ID_MAX = 5;
    static int SESSIONS_ID_MAX = 10000;

    static class Film {
        int id;
        LocalDateTime date;
        Duration time;

        public Film(int id, LocalDateTime date, LocalTime time) {
            this.id = id;
            this.date = date;
            this.time = Duration.ofSeconds(time.getHour()*3600+time.getMinute()*60+time.getSecond());
        }

        @Override
        public String toString() {
            return "Film{" +
                    "id=" + id +
                    ", date=" + date +
                    ", time=" + time +
                    '}';
        }
    }

    static class Session {
        int hall_id;
        LocalDateTime time;
        Film film;

        public Session(int hall_id, LocalDateTime time, Film film) {
            this.hall_id = hall_id;
            this.time = time;
            this.film = film;
        }

        @Override
        public String toString() {
            return "Session{" +
                    "hall_id=" + hall_id +
                    ", time=" + time +
                    ", " + film.toString() +
                    '}';
        }
    }

    static List<List<Integer>> places = new ArrayList<>(1500);
    static List<Film> films = new ArrayList<>(1000);
    static List<Session> sessions = new ArrayList<>(10050);

    static void insert(Table table, List<List<String>> corteges) {
        for (List<String> cortege : corteges) {
            table.INSERT(cortege);
        }
        try {
            table.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(table.tableName + " can't insert " + corteges.size() + " corteges");
        }
    }

    public static void main(String[] args) {

        createDB();
        insert_Slovar(Hall_types_PARAMETERS, Hall_types, 20);
        insert_Slovar(Genres_PARAMETERS, Genres, 50);
        insert_Slovar(Halls_PARAMETERS, Halls, 40);
        insert_Slovar(Statuses_PARAMETERS, Statuses, 40);
        insert_Halls_info();
        try {
            insert_Films();
        } catch (IOException e) {
            e.printStackTrace();
        }
        insert_Films_info();
        try {
            insert_Advertising();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            insert_Newsreels();
        } catch (IOException e) {
            e.printStackTrace();
        }
        insert_Order_ad();
        insert_Places();
        insert_Sessions();
    }

    static void insert_Slovar(String pathString, Table table, int length) {
        Path path = Path.of(pathString);
        try {
            insert(
                    table,
                    new FieldStringLimited(path, length)
                            .getAll()
                            .stream()
                            .map(List::of)
                            .collect(Collectors.toList())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void insert_Halls_info() {
        List<List<String>> res = new ArrayList<>();
        for (int hall_id = 1; hall_id <= HALL_ID_MAX; hall_id++) {
            boolean[] rand = new boolean[] {random.nextBoolean(), random.nextBoolean(), random.nextBoolean()};
            while (!(rand[0] || rand[1] || rand[2])) {
                rand[0] = random.nextBoolean();
                rand[1] = random.nextBoolean();
                rand[2] = random.nextBoolean();
            }
            if (rand[0]) {
                res.add(List.of(String.valueOf(hall_id), String.valueOf(1)));
            } else {
                res.add(List.of(String.valueOf(hall_id), String.valueOf(2)));
            }
            if (rand[1]) {
                res.add(List.of(String.valueOf(hall_id), String.valueOf(3)));
            }
            if (rand[2]) {
                res.add(List.of(String.valueOf(hall_id), String.valueOf(4)));
            } else {
                res.add(List.of(String.valueOf(hall_id), String.valueOf(5)));
            }
        }
        insert(
                Halls_info,
                res
        );
    }

    static void insert_Films() throws IOException {
        List<List<String>> res = new ArrayList<>();
        LocalDateTime time = LocalDateTime.of(2010,1,1,0,0,0);
        Path[] pathsWords = new Path[] {Path.of(RANDOM_PARAMETERS), Path.of(RANDOM_PARAMETERS), Path.of(RANDOM_PARAMETERS)};
        Path[] pathsCountry = new Path[] {Path.of(COUNTRIES)};
        for (int film_id = 1; film_id <= FILM_ID_MAX; film_id++) {
            List<String> cortege = new ArrayList<>();
            FieldStringRandom textField = new FieldStringRandom(pathsWords, 200);
            cortege.add(textField.getRandom());
            cortege.add(String.valueOf(new FieldIntEnum(AGE_RATING).getRandom()));
            cortege.add(textField.getRandom());
            time = time.plusDays(random.nextInt(4));
            cortege.add(time.toString());
            FieldStringRandom country = new FieldStringRandom(pathsCountry, 100);
            cortege.add(country.getRandom());
            int seconds = random.nextInt(3600 * 3) + 100;
            int sec2 = seconds % (60 * 60);
            cortege.add(
                    String.valueOf(seconds / 60 / 60) +
                            ":" +
                            String.valueOf(seconds / 60 % 60) +
                            ":" +
                            String.valueOf(sec2 - (sec2 / 60) * 60)
            );
            cortege.add("Poster");
            cortege.add("Video");

            films.add(new Film(film_id,
                    time,
                    LocalTime.of(
                            seconds / 60 / 60,
                            seconds / 60 % 60,
                    sec2 - (sec2 / 60) * 60)));
            res.add(cortege);
        }
        insert(
                Films,
                res
        );
    }

    static void insert_Films_info() {
        /**
         * ТУТ ИНФО
         */
        List<List<String>> res = new ArrayList<>();
        for (int film_id = 1; film_id <= FILM_ID_MAX; film_id++) {
            for (int genre_id : getRand(random.nextInt(3) + 1, new Integer[] {1,2,3,4,5})) {
                res.add(List.of(String.valueOf(film_id), String.valueOf(genre_id)));
            }
        }
        insert(
                Films_info,
                res
        );
    }

    static void insert_Advertising() throws IOException {
        List<List<String>> res = new ArrayList<>();
        Path[] pathsWords = new Path[] {Path.of(NAMES_AD), Path.of(PREPOSITION), Path.of(COMPANY_NAMES)};
        for (int ad_id = 1; ad_id <= AD_ID_MAX; ad_id++) {
            List<String> cortege = new ArrayList<>();
            FieldStringRandom textField = new FieldStringRandom(pathsWords, 50);
            cortege.add(textField.getRandom());
            cortege.add(String.valueOf(new FieldIntEnum(AGE_RATING).getRandom()));
            cortege.add("Video");
            res.add(cortege);
        }
        insert(
                Advertising,
                res
        );
    }

    static void insert_Newsreels() throws IOException {
        List<List<String>> res = new ArrayList<>();
        Path[] pathsWords = new Path[] {Path.of(RANDOM_PARAMETERS)};
        for (int newsreel_id = 1; newsreel_id <= NEWSREEL_ID_MAX; newsreel_id++) {
            List<String> cortege = new ArrayList<>();
            FieldStringRandom textField = new FieldStringRandom(pathsWords, 40);
            cortege.add(textField.getRandom() + " " + String.valueOf(newsreel_id));
            res.add(cortege);
        }
        insert(
                Newsreels,
                res
        );
    }

    static void insert_Order_ad() {
        List<List<String>> res = new ArrayList<>();
        for (int newsreel_id = 1; newsreel_id <= NEWSREEL_ID_MAX; newsreel_id++) {
            int sizeOfNewsreel = random.nextInt(6) + 10;
            int[] ordering = getRand(sizeOfNewsreel, sizeOfNewsreel);
            for (int i = 1; i <= sizeOfNewsreel; i++) {
                List<String> cortege = new ArrayList<>();
                cortege.add(String.valueOf(newsreel_id));
                cortege.add(String.valueOf(random.nextInt(AD_ID_MAX) + 1));
                cortege.add(String.valueOf(ordering[i - 1]));
                res.add(cortege);
            }
        }
        insert(
                Order_ad,
                res
        );
    }

    static void insert_Places() {
        List<List<String>> res = new ArrayList<>();
        int counter = 1;
        for (int hall_id = 1; hall_id <= HALL_ID_MAX; hall_id++) {
            int row = random.nextInt(4) + 7;
            int seats = random.nextInt(4) + 7;
            for (int i = 1; i <= row; i++) {
                for (int j = 1; j <= seats; j++) {
                    places.add(List.of(hall_id, counter++));
                    List<String> cortege = new ArrayList<>();
                    cortege.add(String.valueOf(hall_id));
                    cortege.add(String.valueOf(i));
                    cortege.add(String.valueOf(j));
                    res.add(cortege);
                }
            }
        }
        insert(
                Places,
                res
        );
    }



    static void insert_Sessions() {
        List<List<String>> res = new ArrayList<>();
        List<LocalDateTime> hallsPerTime = new ArrayList<>(HALL_ID_MAX);
        for (int i = 0; i < HALL_ID_MAX; i++) {
            hallsPerTime.add(LocalDateTime.of(
                    LocalDate.of(2010, 2, 1),
                    LocalTime.of(0, 0, 0)));
        }
        int session_id = 1;
        while (session_id < 10000) {
            for (int i = 0; i < HALL_ID_MAX; i++) {
                Film d;
                if ((d = getLastAccepted(hallsPerTime.get(i))) != null) {
                    List<String> cortege = new ArrayList<>();
                    cortege.add(String.valueOf(i + 1));
                    cortege.add(String.valueOf(d.id));
                    cortege.add(String.valueOf(random.nextInt(1000) + 1));
                    cortege.add(String.valueOf(hallsPerTime.get(i)));
                    if (i == 9900) {
                        current = hallsPerTime.get(i);
                    }
                    Session session = new Session(i + 1, hallsPerTime.get(i), films.get(d.id - 1));
                    System.out.println(session.toString());
                    sessions.add(session);
                    hallsPerTime.set(i, hallsPerTime.get(i).plus(d.time));
                    hallsPerTime.set(i, hallsPerTime.get(i).plusDays(random.nextInt(4) + 1));
                    res.add(cortege);
                    session_id++;
                } else {
                    hallsPerTime.set(i, hallsPerTime.get(i).plusDays(3));
                }
            }
            System.out.println("session_id = " + session_id);
        }
        insert(
                Sessions,
                res
        );
    }

    static LocalDateTime current;

    static class Ticket {

        public Ticket(int ticket_id, int place_id, int status_id, int session_id, String booking) {
            this.ticket_id = ticket_id;
            this.place_id = place_id;
            this.status_id = status_id;
            this.session_id = session_id;
            this.booking = booking;
        }

        int ticket_id;
        int place_id;
        int status_id;
        int session_id;
        String booking;


    }

    static class Log {

        public Log(int ticket_id, int status_id, LocalDateTime time) {
            this.ticket_id = ticket_id;
            this.status_id = status_id;
            this.time = time;
        }

        int ticket_id;
        int status_id;
        LocalDateTime time;


    }

    static List<Integer> getPlaces(int hall_id) {
        return places.stream()
                .filter(x -> x.get(0).equals(hall_id))
                .map(x -> x.get(1))
                .collect(Collectors.toList());
    }

    /**
     * 1 Free
     * 2 Online
     * 3 Offline
     * 4 Reserved
     * 5 Used
     * 6 Not Used
     */

    static void insert_Tickets_Logs() {
        List<List<String>> TicketsRes1 = new ArrayList<>();
        List<List<String>> TicketsRes2 = new ArrayList<>();
        List<List<String>> LogsRes = new ArrayList<>();
        int ticket_id = 1;
        for (int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);
            LocalDateTime sessionTime;
            boolean was = false;
            if (session.time.isBefore(current)) {
                was = true;
                sessionTime = session.time;
            }
            else {
                was = false;
                sessionTime = current;
            }
            List<Map.Entry<Ticket,LocalDateTime>> ticketsAndTimePerSession = new ArrayList<>();
            List<Log> logPerSession = new ArrayList<>();
            int minusMinutes = random.nextInt(24 * 7 * 60) + 24 * 60;
            LocalDateTime addTime = sessionTime.minusMinutes(minusMinutes);
            for (int place_id : getPlaces(session.hall_id)) {
                if (getPercent(98)) {
                    Ticket ticket = new Ticket(ticket_id++, place_id, 1, i + 1, null);
                    ticketsAndTimePerSession.add(Map.entry(ticket, addTime));
                    logPerSession.add(new Log(ticket.ticket_id, ticket.status_id, addTime));
                }
            }

            for (Map.Entry<Ticket,LocalDateTime> ticket : ticketsAndTimePerSession) {
                if (getPercent(50)) {
                    if (getPercent(10)) {
                        ticket.getKey().status_id = 4;
                        ticket.getKey().booking = "XXXXX";
                        ticket.setValue(ticket.getValue().plusMinutes(random.nextInt(minusMinutes / 3)));
                        logPerSession.add(new Log(ticket.getKey().ticket_id, ticket.getKey().status_id, ticket.getValue()));
                        if (getPercent(60)) {
                            ticket.getKey().status_id = random.nextInt(2) + 2;
                            ticket.setValue(ticket.getValue().plusMinutes(random.nextInt(minusMinutes / 3)));
                            logPerSession.add(new Log(ticket.getKey().ticket_id, ticket.getKey().status_id, ticket.getValue()));
                        } else {
                            ticket.getKey().status_id = 1;
                            ticket.setValue(sessionTime.minusMinutes(40));
                            logPerSession.add(new Log(ticket.getKey().ticket_id, ticket.getKey().status_id, ticket.getValue()));
                            if (getPercent(50)) {
                                ticket.getKey().status_id = random.nextInt(2) + 2;
                                ticket.setValue(ticket.getValue().plusMinutes(random.nextInt(39)));
                                logPerSession.add(new Log(ticket.getKey().ticket_id, ticket.getKey().status_id, ticket.getValue()));
                            }
                        }
                    }
                    else {
                        ticket.getKey().status_id = random.nextInt(2) + 2;
                        ticket.setValue(ticket.getValue().plusMinutes(random.nextInt(minusMinutes / 3)));
                        logPerSession.add(new Log(ticket.getKey().ticket_id, ticket.getKey().status_id, ticket.getValue()));
                        if (getPercent(10)) {
                            ticket.getKey().status_id = 1;
                            ticket.setValue(ticket.getValue().plusMinutes(random.nextInt(minusMinutes / 3)));
                            logPerSession.add(new Log(ticket.getKey().ticket_id, ticket.getKey().status_id, ticket.getValue()));
                        }
                    }
                }
            }

            if (was) {
                for (Map.Entry<Ticket,LocalDateTime> ticket : ticketsAndTimePerSession) {
                    if (ticket.getKey().status_id == 2 || ticket.getKey().status_id == 3) {
                        if (getPercent(95)) {
                            ticket.getKey().status_id = 5;
                            ticket.setValue(sessionTime.plus(session.film.time));
                            logPerSession.add(new Log(ticket.getKey().ticket_id, ticket.getKey().status_id, ticket.getValue()));
                        } else {
                            ticket.getKey().status_id = 6;
                            ticket.setValue(sessionTime.plus(session.film.time));
                            logPerSession.add(new Log(ticket.getKey().ticket_id, ticket.getKey().status_id, ticket.getValue()));
                        }
                    }
                }

            }

            for (Log log : logPerSession) {
                List<String> cortage = new ArrayList<>();
                cortage.add(String.valueOf(log.ticket_id));
                cortage.add(String.valueOf(log.status_id));
                cortage.add(String.valueOf(log.time));
                LogsRes.add(cortage);
            }

            for (Map.Entry<Ticket,LocalDateTime> ticket : ticketsAndTimePerSession) {
                List<String> cortage = new ArrayList<>();
                cortage.add(String.valueOf(ticket.getKey().place_id));
                cortage.add(String.valueOf(ticket.getKey().status_id));
                cortage.add(String.valueOf(ticket.getKey().session_id));
                if (ticket.getKey().booking == null) {
                    TicketsRes1.add(cortage);
                } else {
                    cortage.add(String.valueOf(ticket.getKey().booking));
                    TicketsRes2.add(cortage);
                }
            }
        }

        insert(
                Tickets1,
                TicketsRes1
        );
        insert(
                Tickets2,
                TicketsRes2
        );
        insert(
                Logs,
                LogsRes
        );
    }

    /**
     *
     * @param i = 0..100
     * @return
     */
    static boolean getPercent(int i) {
        return random.nextInt(100) < i;
    }

    static Film getLastAccepted(LocalDateTime time) {
        return films.stream()
                .filter(x -> x.date.isBefore(time))
                .reduce((s1, s2) -> s2)
                .orElse(null);
    }


    static void createDB() {
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            Statement statement = conn.createStatement();
            for (String creator : creation) {
                statement.executeUpdate(creator);
            }
            System.out.println("Database has been created!");
        } catch(Exception ex){
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    static int[] getRand(int count, int size) {
        List<Integer> list = IntStream.rangeClosed(1, size)
                .boxed()
                .collect(Collectors.toList());
        int[] res = new int[count];
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(list.size());
            res[i] = list.get(index);
            list.remove(index);
        }
        return res;
    }

    static int[] getRand(int count, Integer[] arr) {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(arr));
        int[] res = new int[count];
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(list.size());
            res[i] = list.get(index);
            list.remove(index);
        }
        return res;
    }

}