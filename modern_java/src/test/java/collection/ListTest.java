package collection;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListTest {

    /**
     * Arrays#asList로 생성한 리스트는 변경 불가(요소 수정 제외)
     */
    @Test
    public void list_UnsupportedOperationException() throws Exception {
        //given
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        //when
        list.set(0, 10);

        //then
        assertTrue(list.get(0).equals(10)); //요소 수정 가능3
        assertThrows(UnsupportedOperationException.class, () -> {
            list.add(20); //collection 변경 불가
        });
    }

    /**
     * removeIf() 이용하여 predicate로 요소 제거 가능
     * replaceAll() 이용하여 모든 요소 변경 가능
     */
    @Test
    public void list_modify() throws Exception {
        //given
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(3);

        //when
        list.removeIf(val -> val.equals(1)); //조건이 맞다면 제거
        list.replaceAll(val -> val + 10);

        //then
        assertTrue(list.equals(Arrays.asList(12, 12, 13, 13)));
    }

    /**
     * Map의 key, value를 이용한 정렬
     */
    @Test
    public void map_sort() throws Exception {
        //given
        Map<Integer, String> map = new HashMap<>();

        //when
        map.put(1, "one");
        map.put(3, "three");
        map.put(2, "two");
        map.put(4, "four");

        System.out.println(map);

        //then
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(System.out::println);

        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }

    /**
     * 값 없을 때 default 값
     */
    @Test
    public void map_get() throws Exception {
        //given
        Map<Integer, String> map = new HashMap<>();

        //when
        map.put(1, "one");
        map.put(3, "three");
        map.put(2, "two");
        map.put(4, "four");

        //then
        assertTrue(map.getOrDefault(10, "ten").equals("ten"));
        map.computeIfAbsent(0, (key) -> "add_" + key);
        map.computeIfAbsent(1, (key) -> "add_" + key);
        map.computeIfPresent(2, (key, val) -> key + "_" + val);
        System.out.println(map);
        //{1=one, 3=three, 2=2_two, 4=four, 0=add_0}
    }

    /**
     * putAll, merge를 이용해 map을 합칠수 있다.
     * merge의 람다 param을 이용하여 동작을 지정 할 수 있다.
     */
    @Test
    public void map_merge() throws Exception {
        //given
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        HashMap<Integer, String> merge = new HashMap<>();
        merge.put(3, "three");
        merge.put(4, "four");

        HashMap<Integer, String> merge2 = new HashMap<>();
        merge2.put(2, "two");
        merge2.put(3, "three");
        merge2.put(4, "four");

        //when
        merge.putAll(map);
        map.forEach((k, v) -> merge2.merge(k, v, (v1, v2) -> v1 + "_" + v2));

        //then
        System.out.println(merge);
        //{1=one, 2=two, 3=three, 4=four}
        System.out.println(merge2);
        //{1=one, 2=two_two, 3=three, 4=four}
    }

    @Test
    public void concurrentHashMap() throws Exception {
        //given
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();

        //when
        long size = map.mappingCount();

        //then
        System.out.println(size);
    }
}