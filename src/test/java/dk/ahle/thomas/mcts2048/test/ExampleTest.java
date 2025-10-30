package dk.ahle.thomas.mcts2048.test;

import dk.ahle.thomas.mcts2048.Board;
import dk.ahle.thomas.mcts2048.strategy.Strategy;
import dk.ahle.thomas.mcts2048.strategy.RandomStrategy;
import dk.ahle.thomas.mcts2048.measure.SumMeasure;
import dk.ahle.thomas.mcts2048.measure.FreesMeasure;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 示例测试类 - 展示JUnit 5和Mockito的使用
 * 
 * 本测试类用于教学目的，演示：
 * 1. JUnit 5基本注解的使用
 * 2. 参数化测试
 * 3. Mockito模拟对象
 * 4. 断言方法
 */
@DisplayName("Board功能测试示例")
public class ExampleTest {

    private Board board;
    
    @Mock
    private Strategy mockStrategy;

    /**
     * 在每个测试方法执行前运行
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        board = new Board();
    }

    /**
     * 在每个测试方法执行后运行
     */
    @AfterEach
    public void tearDown() {
        board = null;
    }

    /**
     * 基本测试示例
     */
    @Test
    @DisplayName("测试新建的Board不为空")
    public void testBoardCreation() {
        assertNotNull(board, "Board对象不应为null");
    }

    /**
     * 测试Board的基本属性
     */
    @Test
    @DisplayName("测试Board的初始状态")
    public void testBoardInitialState() {
        Board newBoard = new Board();
        assertNotNull(newBoard);
        // 可以添加更多关于初始状态的断言
    }

    /**
     * 使用Mockito模拟对象的示例
     */
    // TODO: "Something is wrong with Mockito."
    // @Test
    // @DisplayName("测试Strategy接口的模拟")
    // public void testMockStrategy() {
    //     // 设置mock对象的行为
    //     when(mockStrategy.move(any(Board.class))).thenReturn(Board.UP);
        
    //     // 执行测试
    //     int move = mockStrategy.move(board);
        
    //     // 验证结果
    //     assertEquals(Board.UP, move);
        
    //     // 验证方法被调用
    //     verify(mockStrategy, times(1)).move(any(Board.class));
    // }

    /**
     * 测试异常情况
     */
    @Test
    @DisplayName("测试异常处理")
    public void testExceptionHandling() {
        // 示例：测试某个方法在特定条件下抛出异常
        // assertThrows(IllegalArgumentException.class, () -> {
        //     board.someMethodThatThrowsException();
        // });
        
        // 这里只是示例，实际需要根据代码逻辑调整
        assertDoesNotThrow(() -> {
            Board newBoard = new Board();
        });
    }

    /**
     * 测试Measure功能
     */
    @Test
    @DisplayName("测试SumMeasure")
    public void testSumMeasure() {
        SumMeasure measure = new SumMeasure();
        double score = measure.score(board);
        assertTrue(score >= 0, "分数应该大于等于0");
    }

    /**
     * 测试FreesMeasure功能
     */
    @Test
    @DisplayName("测试FreesMeasure")
    public void testFreesMeasure() {
        FreesMeasure measure = new FreesMeasure();
        double score = measure.score(board);
        assertTrue(score >= 0 && score <= 16, "空闲位置数应该在0-16之间");
    }

    /**
     * 性能测试示例
     */
    // TODO: "Something is wrong with Mockito."
    // @Test
    // @DisplayName("测试RandomStrategy的性能")
    // @Timeout(5) // 超时5秒
    // public void testStrategyPerformance() {
    //     Strategy strategy = new RandomStrategy();
    //     Board testBoard = new Board();
        
    //     // 执行多次移动
    //     for (int i = 0; i < 10; i++) {
    //         int move = strategy.move(testBoard);
    //         testBoard = testBoard.move(move);
    //         if (testBoard.isTerminal()) {
    //             break;
    //         }
    //     }
        
    //     assertTrue(true, "性能测试完成");
    // }

    /**
     * 禁用的测试示例
     */
    @Test
    @Disabled("此测试暂时禁用，等待功能完善")
    @DisplayName("待完善的测试")
    public void testDisabled() {
        fail("这个测试不应该运行");
    }

    /**
     * Mock演示测试用例
     */
    @Test
    @DisplayName("Mock演示：模拟Measure接口并验证调用")
    public void testMockMeasureDemo() {
        // 1. 创建Mock对象
        dk.ahle.thomas.mcts2048.measure.Measure mockMeasure = 
            mock(dk.ahle.thomas.mcts2048.measure.Measure.class);
        
        // 2. 定义Mock行为：当调用score方法时返回固定值
        when(mockMeasure.score(any(Board.class))).thenReturn(100.0);
        
        // 3. 使用Mock对象
        Board testBoard = new Board();
        double score = mockMeasure.score(testBoard);
        
        // 4. 验证返回值
        assertEquals(100.0, score, "Mock返回的分数应该是100.0");
        
        // 5. 验证方法被调用了一次
        verify(mockMeasure, times(1)).score(any(Board.class));
        
        // 6. 可以多次调用并验证
        mockMeasure.score(testBoard);
        mockMeasure.score(testBoard);
        
        // 7. 验证总共被调用了3次
        verify(mockMeasure, times(3)).score(any(Board.class));
    }

    /**
     * Mock演示测试用例
     */
    @Test
    @DisplayName("Mock演示：模拟多次调用返回不同值")
    public void testMockWithMultipleReturnValues() {
        // 创建Mock对象
        dk.ahle.thomas.mcts2048.measure.Measure mockMeasure = 
            mock(dk.ahle.thomas.mcts2048.measure.Measure.class);
        
        // 定义多次调用的返回值
        when(mockMeasure.score(any(Board.class)))
            .thenReturn(100.0)  // 第一次调用返回100.0
            .thenReturn(200.0)  // 第二次调用返回200.0
            .thenReturn(300.0); // 第三次调用返回300.0
        
        Board testBoard = new Board();
        
        // 验证不同的返回值
        assertEquals(100.0, mockMeasure.score(testBoard));
        assertEquals(200.0, mockMeasure.score(testBoard));
        assertEquals(300.0, mockMeasure.score(testBoard));
        
        // 验证被调用了3次
        verify(mockMeasure, times(3)).score(any(Board.class));
    }

    /**
     * Mock演示测试用例
     */
    @Test
    @DisplayName("Mock演示：验证特定参数的方法调用")
    public void testMockWithSpecificArgument() {
        // 创建Mock对象
        dk.ahle.thomas.mcts2048.measure.Measure mockMeasure = 
            mock(dk.ahle.thomas.mcts2048.measure.Measure.class);
        
        // 定义Mock行为
        when(mockMeasure.score(any(Board.class))).thenReturn(50.0);
        
        // 创建特定的Board对象
        Board specificBoard = new Board();
        
        // 调用方法
        double score = mockMeasure.score(specificBoard);
        
        // 验证返回值
        assertEquals(50.0, score);
        
        // 验证使用特定参数调用
        verify(mockMeasure).score(specificBoard);
        
        // 验证没有其他交互
        verifyNoMoreInteractions(mockMeasure);
    }
}
