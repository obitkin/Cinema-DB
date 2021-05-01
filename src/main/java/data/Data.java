package data;

import tables.Table;

public interface Data {

    String parametersDir = "src/main/resources/tables/parameters";
    String randomDir = "src/main/resources/tables/random";

    String RANDOM_PARAMETERS = randomDir + "/" + "linuxwords.txt";
    String COUNTRIES = randomDir + "/" + "countries";

    Table Hall_types = new Table(
            "Hall_types",
            new String[] {"description"});
    String Hall_types_PARAMETERS = parametersDir + "/" + "Hall_types";

    Table Halls = new Table(
            "Halls",
            new String[] {"name"});
    String Halls_PARAMETERS = parametersDir + "/" + "Halls";

    Table Genres = new Table(
            "Genres",
            new String[] {"name"});
    String Genres_PARAMETERS = parametersDir + "/" + "Genres";

    Table Statuses = new Table(
            "Statuses",
            new String[] {"description"});
    String Statuses_PARAMETERS = parametersDir + "/" + "Statuses";

    Table Halls_info = new Table(
            "Halls_info",
            new String[] {"hall_id", "hall_type_id"});

    Table Places = new Table(
            "Places",
            new String[] {"hall_id", "row", "seat"});

    Table Films = new Table(
            "Films",
            new String[] {"name", "age_rating" , "description", "time", "country", "duration", "poster", "video"});

    Table Films_info = new Table(
            "Films_info",
            new String[] {"film_id", "genre_id"});

    Table Advertising = new Table(
            "Advertising",
            new String[] {"name", "age_rating", "video"});
    String NAMES_AD = randomDir + "/" + "Names_AD";
    String PREPOSITION = randomDir + "/" + "Preposition_AD";
    String COMPANY_NAMES = randomDir + "/" + "Company_AD";

    Table Newsreels = new Table(
            "Newsreels",
            new String[] {"name"});

    Table Order_ad = new Table(
            "Order_ad",
            new String[] {"newsreel_id", "ad_id", "ordering"});

    Table Sessions = new Table(
            "Sessions",
            new String[] {"hall_id", "film_id", "newsreel_id", "time"});

    Table Tickets1 = new Table(
            "Tickets",
            new String[] {"place_id", "status_id", "session_id", "booking_code"});

    Table Tickets2 = new Table(
            "Tickets",
            new String[] {"place_id", "status_id", "session_id"});

    Table Logs = new Table(
            "Logs",
            new String[] {"ticket_id", "status_id", "change_time"});

    String url = "jdbc:mysql://localhost/cinema";
    String username = "root";
    String password = "After1901";

}

