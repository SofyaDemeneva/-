import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Book {
    private int id;
    private String title;
    private String author;
    private int year;
    private String genre;
    private double price;
    private String currency;
    private String format;
    private String language;
    private String publisherName;
    private String publisherCity;
    private String publisherCountry;
    private String translator;
    private String award;

    public Book(int id) {
        this.id = id;
    }

    public void setField(String field, String value) {
        switch (field) {
            case "title" -> this.title = value;
            case "author" -> this.author = value;
            case "year" -> this.year = Integer.parseInt(value);
            case "genre" -> this.genre = value;
            case "price" -> this.price = Double.parseDouble(value);
            case "currency" -> this.currency = value;
            case "format" -> this.format = value;
            case "language" -> this.language = value;
            case "publisherName" -> this.publisherName = value;
            case "publisherCity" -> this.publisherCity = value;
            case "publisherCountry" -> this.publisherCountry = value;
            case "translator" -> this.translator = value;
            case "award" -> this.award = value;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Book ID: ").append(id)
                .append("\nTitle: ").append(title)
                .append("\nAuthor: ").append(author)
                .append("\nYear: ").append(year)
                .append("\nGenre: ").append(genre)
                .append("\nPrice: ").append(price).append(currency != null ? " " + currency : "")
                .append("\nLanguage: ").append(language);

        if (publisherName != null) {
            result.append("\nPublisher: ").append(publisherName);
        }
        if (publisherCity != null) {
            result.append("\nPublisher City: ").append(publisherCity);
        }
        if (publisherCountry != null) {
            result.append("\nPublisher Country: ").append(publisherCountry);
        }
        if (format != null) {
            result.append("\nFormat: ").append(format);
        }
        if (translator != null) {
            result.append("\nTranslator: ").append(translator);
        }
        if (award != null) {
            result.append("\nAward: ").append(award);
        }

        return result.append("\n").toString();
    }
}

class Library {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Book book : books) {
            result.append(book).append("\n");
        }
        return result.toString();
    }
}

public class XMLParser {
    public static void main(String[] args) {
        String xml = readFile("C:/Users/sofya/Downloads/data/data/random_structure_7.xml");

        if (!xml.isEmpty()) {
            Library library = new Library();
            parseXML(xml, library);
            System.out.println(library);
        } else {
            System.out.println("Ошибка чтения файла.");
        }
    }

    private static String readFile(String filename) {
        StringBuilder xmlContent = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                xmlContent.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filename);
        }
        return xmlContent.toString();
    }

    private static void parseXML(String xml, Library library) {
        Matcher matcher = Pattern.compile("<book id=\"(\\d+)\">(.*?)</book>", Pattern.DOTALL).matcher(xml);
        while (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            Book book = new Book(id);

            String bookXml = matcher.group(2);
            extractAndSet(bookXml, "title", book);
            extractAndSet(bookXml, "author", book);
            extractAndSet(bookXml, "year", book);
            extractAndSet(bookXml, "genre", book);
            extractAndSet(bookXml, "price", book);  // price содержит цену
            extractAndSet(bookXml, "format", book);
            extractAndSet(bookXml, "language", book);
            parsePublisher(bookXml, book);  // Парсинг сложного тега publisher
            extractAndSet(bookXml, "translator", book);
            extractAndSetAward(bookXml, book);  // Парсинг награды

            library.addBook(book);
        }
    }

    private static void parsePublisher(String xml, Book book) {
        // Находим название издателя, город и страну
        String publisherName = getTagValue(xml, "name");
        String city = getTagValue(xml, "city");
        String country = getTagValue(xml, "country");

        if (!publisherName.isEmpty()) {
            book.setField("publisherName", publisherName);
        }
        if (!city.isEmpty()) {
            book.setField("publisherCity", city);
        }
        if (!country.isEmpty()) {
            book.setField("publisherCountry", country);
        }
    }

    private static void extractAndSet(String xml, String tag, Book book) {
        Matcher matcher = Pattern.compile("<" + tag + "(.*?)>(.*?)</" + tag + ">", Pattern.DOTALL).matcher(xml);
        if (matcher.find()) {
            String attributes = matcher.group(1);
            String value = matcher.group(2);

            if (tag.equals("price")) {
                book.setField("price", value);
                Matcher currencyMatcher = Pattern.compile("currency=\"(.*?)\"").matcher(attributes);
                if (currencyMatcher.find()) {
                    book.setField("currency", currencyMatcher.group(1));
                }
            } else {
                book.setField(tag, value);
            }
        }
    }

    private static void extractAndSetAward(String xml, Book book) {
        Matcher matcher = Pattern.compile("<award>(.*?)</award>", Pattern.DOTALL).matcher(xml);
        if (matcher.find()) {
            book.setField("award", matcher.group(1).trim());
        }
    }

    private static String getTagValue(String xml, String tag) {
        Matcher matcher = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL).matcher(xml);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}
