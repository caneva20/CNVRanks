package me.caneva20.CNVRanks;

@SuppressWarnings("CanBeFinal")
public class SQLS {
    private SQLS () {}

    public static String createTableCnvRanks =
            "CREATE TABLE IF NOT EXISTS cnvranks(" +
                    "`id` int(255) NOT NULL AUTO_INCREMENT," +
                    "`player` VARCHAR(255) NOT NULL," +
                    "`rank` VARCHAR(255) NOT NULL," +
                    "`exp` DOUBLE NOT NULL," +
                    "PRIMARY KEY `id` (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
}
