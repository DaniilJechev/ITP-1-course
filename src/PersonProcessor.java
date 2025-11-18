public class PersonProcessor {

    public static void main(String[] args) {
        Person person1 = new Person();
        Person person2 = new Person("Munir", 32);
        System.out.println(person1.toString());
        System.out.println(person2);
    }

}

class Person {
    private String name;
    private int age;

    public Person() {
        age = 0;
        name = "Default";
    }
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
