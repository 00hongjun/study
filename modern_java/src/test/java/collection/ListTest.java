package collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
}