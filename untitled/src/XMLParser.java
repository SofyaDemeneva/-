import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Класс для представления цены
class Price {
    private double value; // Значение цены
    private String currency; // Валюта цены

    // Геттеры и сеттеры для поля value
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // Геттеры и сеттеры для поля currency
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Переопределение метода toString для вывода информации о цене
    @Override
    public String toString() {
        return value + (currency != null ? " " + currency : "");
    }
}

// Класс, представляющий книгу с различными атрибутами
class Book {
    private int id;
    private String title;
    private String author;
    private int year;
    private String genre;
    private Price price; // Объект класса Price для цены
    private String format;
    private String language;
    private Publisher publisher; // Объект класса Publisher
    private String translator;
    private String award;

    // Конструктор для создания книги по её ID
    public Book(int id) {
        this.id = id;
    }

    // Геттеры и сеттеры для всех полей
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    // Переопределение метода toString для вывода информации о книге
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Book ID: ").append(id)
                .append("\nTitle: ").append(title)
                .append("\nAuthor: ").append(author)
                .append("\nYear: ").append(year)
                .append("\nGenre: ").append(genre)
                .append("\nPrice: ").append(price != null ? price : "")
                .append("\nLanguage: ").append(language);

        if (publisher != null) {
            result.append("\n").append(publisher); // Вызов метода toString() класса Publisher
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

// Класс Publisher для сложного тега <publisher>
class Publisher {
    private String name; // Название издателя
    private String city; // Город издателя
    private String country; // Страна издателя

    // Геттеры и сеттеры для полей издателя
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Переопределение метода toString для вывода информации об издателе
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Publisher: \n")
                .append("\tName: ").append(name != null ? name : "").append("\n")
                .append("\tCity: ").append(city != null ? city : "").append("\n")
                .append("\tCountry: ").append(country != null ? country : "");
        return result.toString();
    }
}

// Класс Library для хранения книг
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

// Главный класс программы для парсинга XML
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

    // Метод для чтения XML-файла
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

    // Метод для парсинга XML и заполнения объекта Library
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
            extractAndSetPrice(bookXml, book); // Обрабатываем цену с помощью нового метода
            extractAndSet(bookXml, "format", book);
            extractAndSet(bookXml, "language", book);
            parsePublisher(bookXml, book); // Парсинг тега <publisher> для сложной структуры
            extractAndSet(bookXml, "translator", book);
            extractAndSetAward(bookXml, book);

            library.addBook(book);
        }
    }

    // Парсинг сложного тега <publisher>
    private static void parsePublisher(String xml, Book book) {
        Publisher publisher = new Publisher();
        publisher.setName(getTagValue(xml, "name"));
        publisher.setCity(getTagValue(xml, "city"));
        publisher.setCountry(getTagValue(xml, "country"));
        book.setPublisher(publisher); // Устанавливаем объект Publisher в объект Book
    }

    // Парсинг цены с использованием класса Price
    private static void extractAndSetPrice(String xml, Book book) {
        Matcher matcher = Pattern.compile("<price(.*?)>(.*?)</price>", Pattern.DOTALL).matcher(xml);
        if (matcher.find()) {
            Price price = new Price();
            String attributes = matcher.group(1);
            String value = matcher.group(2);
            price.setValue(Double.parseDouble(value));

            // Извлекаем валюту из атрибута тега <price>
            Matcher currencyMatcher = Pattern.compile("currency=\"(.*?)\"").matcher(attributes);
            if (currencyMatcher.find()) {
                price.setCurrency(currencyMatcher.group(1));
            }

            book.setPrice(price); // Устанавливаем объект Price в объект Book
        }
    }

    // Метод для извлечения данных и их установки в объект Book
    private static void extractAndSet(String xml, String tag, Book book) {
        Matcher matcher = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL).matcher(xml);
        if (matcher.find()) {
            String value = matcher.group(1).trim();

            switch (tag) {
                case "title":
                    book.setTitle(value);
                    break;
                case "author":
                    book.setAuthor(value);
                    break;
                case "year":
                    book.setYear(Integer.parseInt(value));
                    break;
                case "genre":
                    book.setGenre(value);
                    break;
                case "format":
                    book.setFormat(value);
                    break;
                case "language":
                    book.setLanguage(value);
                    break;
                case "translator":
                    book.setTranslator(value);
                    break;
                case "award":
                    book.setAward(value);
                    break;
            }
        }
    }

    // Извлечение значения тега из XML
    private static String getTagValue(String xml, String tag) {
        Matcher matcher = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL).matcher(xml);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    // Извлечение награды
    private static void extractAndSetAward(String xml, Book book) {
        Matcher matcher = Pattern.compile("<award>(.*?)</award>", Pattern.DOTALL).matcher(xml);
        if (matcher.find()) {
            book.setAward(matcher.group(1).trim());
        }
    }
}
