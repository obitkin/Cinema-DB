import data.CreationQuery;
import data.Data;
import providers.fields.FieldIntEnum;
import providers.fields.FieldIntGap;
import providers.fields.FieldStringLimited;
import providers.fields.FieldStringRandom;
import tables.Table;

import java.io.IOException;
import java.nio.file.Path;
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
    }

    static List<List<Integer>> places = new ArrayList<>(1500);
    static List<Film> films = new ArrayList<>(1000);

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
        LocalDateTime time = LocalDateTime.of(2014,1,1,0,0,1);
        Path[] pathsWords = new Path[] {Path.of(RANDOM_PARAMETERS), Path.of(RANDOM_PARAMETERS), Path.of(RANDOM_PARAMETERS)};
        Path[] pathsCountry = new Path[] {Path.of(COUNTRIES)};
        for (int film_id = 1; film_id <= FILM_ID_MAX; film_id++) {
            List<String> cortege = new ArrayList<>();
            FieldStringRandom textField = new FieldStringRandom(pathsWords, 200);
            cortege.add(textField.getRandom());
            cortege.add(String.valueOf(new FieldIntEnum(AGE_RATING).getRandom()));
            cortege.add(textField.getRandom());
            time = time.plusDays(random.nextInt(2));
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
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2014, 1, 1), LocalTime.of(0, 0, 1));
        for (int session_id = 1; session_id <= SESSIONS_ID_MAX; session_id++) {
            //Date d = new Time(1,1,1).toInstant();
        }
        insert(
                Sessions,
                res
        );
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