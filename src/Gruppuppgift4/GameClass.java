package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class GameClass {

    List<Questions> completeList = new ArrayList<>();
    Set<String> listOfCategory = new HashSet<String>();

    public GameClass() {
        readList();
        checkCategorys(completeList);
    }

    public List<Questions> readList() {
        Path path = Path.of("src/Gruppuppgift4/questions");
        try (BufferedReader br = new BufferedReader(new FileReader(path.toString()))) {
            String input;
            while ((input = br.readLine()) != null) {
                String[] ql = input.split(";");
                Questions quest = new Questions(ql[0], ql[1], ql[2], ql[3], ql[4], ql[5]);
                completeList.add(quest);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return completeList;
    }

    public Set<String> checkCategorys(List<Questions> questions) {
        listOfCategory.clear();
        for (Questions q : completeList) {
            if(q.unused)
                listOfCategory.add(q.category);
            }
        return listOfCategory;
    }

    public Questions getQuestions(String category, List<Questions> completeList ) {
        Collections.shuffle(completeList);
        for (Questions q : completeList) {
            if (q.category.equalsIgnoreCase(category) && q.unused ) {
                int i = completeList.indexOf(q);
                q.unused = false;
                completeList.get(i).unused = false;
                return completeList.get(i);
            }
        } return null;
    }
}