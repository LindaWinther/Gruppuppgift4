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
        checkCategorys();
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

    public Set<String> checkCategorys() {
        for (Questions q : completeList) {
            listOfCategory.add(q.category);
        }
        return listOfCategory;
    }

    //todo Försökt mixtra i denna men gått bet...
    public List<String> getAvailableCategories(int number) {
        List <String> list = new ArrayList<>();
//        list.clear();
        for (Questions q : completeList) {
            if (q.unused) {
                String cat = q.category;


                int index;
                if (!list.contains(cat)) {
                    int count = 0;
                    for (Questions q2 : completeList) {
                        if (q2.isUnused() && q2.category.equalsIgnoreCase(cat)) {
                            count++;
                            index = q2.category.indexOf(q.category);
                            System.out.println(index);



                        }
                    }
                    if (count >= number) {
                        list.add(cat);


//                        q2.setUnused(false);
//                        q2.unused = false;
                        System.out.println("added" + list.size());
//                    }else {
//                        list.remove(cat);
//                        System.out.println("removed" + list.size());
//                    }
                    }
//                else {
//                    list.remove(cat);
//                    System.out.println("removed botten" + list.size());
//                }
                }
            }


        }System.out.println("list" + list.size());
        return list;
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
//    public Questions getNextQ(List<Questions> list) {
//        for (Questions q : list) {
//            if (q.unused) {
//                 int i = list.indexOf(q);
//                 list.get(i).unused = false;
//
//                return list.get(i);
//            }
//
//        }return null;
//    }