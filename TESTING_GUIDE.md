# 单元测试教学指南

本文档为学生提供使用JUnit 5、Mockito、JaCoCo和PITest进行单元测试的完整指南。

## 1. JUnit 5 基础

### 1.1 基本注解

```java
import org.junit.jupiter.api.*;

public class MyTest {
    
    @BeforeAll  // 在所有测试前执行一次（静态方法）
    static void setUpAll() {
        // 初始化资源
    }
    
    @BeforeEach  // 在每个测试前执行
    void setUp() {
        // 准备测试数据
    }
    
    @Test  // 标记测试方法
    void myTest() {
        // 测试逻辑
    }
    
    @AfterEach  // 在每个测试后执行
    void tearDown() {
        // 清理资源
    }
    
    @AfterAll  // 在所有测试后执行一次（静态方法）
    static void tearDownAll() {
        // 释放资源
    }
}
```

### 1.2 常用断言

```java
import static org.junit.jupiter.api.Assertions.*;

@Test
void testAssertions() {
    // 相等性断言
    assertEquals(expected, actual);
    assertEquals(expected, actual, "失败时的消息");
    
    // 真假断言
    assertTrue(condition);
    assertFalse(condition);
    
    // 空值断言
    assertNull(object);
    assertNotNull(object);
    
    // 数组断言
    assertArrayEquals(expectedArray, actualArray);
    
    // 异常断言
    assertThrows(IllegalArgumentException.class, () -> {
        // 应该抛出异常的代码
    });
    
    // 超时断言
    assertTimeout(Duration.ofSeconds(1), () -> {
        // 应该在1秒内完成的代码
    });
    
    // 组合断言
    assertAll("person",
        () -> assertEquals("John", person.getFirstName()),
        () -> assertEquals("Doe", person.getLastName())
    );
}
```

### 1.3 参数化测试

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

class ParameterizedTests {
    
    // 使用单个值
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testWithInts(int number) {
        assertTrue(number > 0);
    }
    
    // 使用字符串
    @ParameterizedTest
    @ValueSource(strings = {"hello", "world"})
    void testWithStrings(String word) {
        assertFalse(word.isEmpty());
    }
    
    // 使用CSV数据
    @ParameterizedTest
    @CsvSource({
        "1, 2, 3",
        "10, 20, 30",
        "100, 200, 300"
    })
    void testWithCsv(int a, int b, int sum) {
        assertEquals(sum, a + b);
    }
    
    // 从CSV文件读取
    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv")
    void testWithCsvFile(String input, String expected) {
        assertEquals(expected, process(input));
    }
    
    // 使用方法提供数据
    @ParameterizedTest
    @MethodSource("provideTestData")
    void testWithMethodSource(String input, int expected) {
        assertEquals(expected, input.length());
    }
    
    static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of("hello", 5),
            Arguments.of("world", 5),
            Arguments.of("test", 4)
        );
    }
}
```

### 1.4 嵌套测试

```java
@DisplayName("Board测试")
class BoardTest {
    
    @Nested
    @DisplayName("创建测试")
    class CreationTests {
        
        @Test
        @DisplayName("测试默认构造函数")
        void testDefaultConstructor() {
            // 测试代码
        }
    }
    
    @Nested
    @DisplayName("移动测试")
    class MoveTests {
        
        @Test
        void testMoveUp() {
            // 测试代码
        }
    }
}
```

## 2. Mockito 使用指南

### 2.1 创建Mock对象

```java
import org.mockito.*;
import static org.mockito.Mockito.*;

class MockitoTest {
    
    // 使用注解创建Mock
    @Mock
    private Strategy mockStrategy;
    
    // 使用注解注入Mock
    @InjectMocks
    private GameController controller;
    
    @BeforeEach
    void setUp() {
        // 初始化Mock对象
        MockitoAnnotations.openMocks(this);
        
        // 或者手动创建Mock
        Strategy strategy = mock(Strategy.class);
    }
}
```

### 2.2 定义Mock行为

```java
@Test
void testMockBehavior() {
    // 简单返回值
    when(mockStrategy.move(any(Board.class))).thenReturn(Board.UP);
    
    // 多次调用返回不同值
    when(mockStrategy.move(any(Board.class)))
        .thenReturn(Board.UP)
        .thenReturn(Board.DOWN)
        .thenReturn(Board.LEFT);
    
    // 抛出异常
    when(mockStrategy.move(null))
        .thenThrow(new IllegalArgumentException());
    
    // 使用Answer自定义返回
    when(mockStrategy.move(any(Board.class)))
        .thenAnswer(invocation -> {
            Board board = invocation.getArgument(0);
            return board.isTerminal() ? -1 : Board.UP;
        });
}
```

### 2.3 验证方法调用

```java
@Test
void testVerification() {
    // 执行测试
    controller.play();
    
    // 验证方法被调用
    verify(mockStrategy).move(any(Board.class));
    
    // 验证调用次数
    verify(mockStrategy, times(3)).move(any(Board.class));
    verify(mockStrategy, atLeast(1)).move(any(Board.class));
    verify(mockStrategy, atMost(5)).move(any(Board.class));
    verify(mockStrategy, never()).reset();
    
    // 验证调用顺序
    InOrder inOrder = inOrder(mockStrategy);
    inOrder.verify(mockStrategy).move(any(Board.class));
    inOrder.verify(mockStrategy).move(any(Board.class));
    
    // 验证没有更多交互
    verifyNoMoreInteractions(mockStrategy);
}
```

### 2.4 Spy对象（部分模拟）

```java
@Test
void testSpy() {
    // 创建真实对象的spy
    RandomStrategy realStrategy = new RandomStrategy();
    RandomStrategy spyStrategy = spy(realStrategy);
    
    // 默认调用真实方法
    int move = spyStrategy.move(board);
    
    // 可以stub部分方法
    when(spyStrategy.move(any(Board.class))).thenReturn(Board.UP);
    
    // 验证调用
    verify(spyStrategy).move(any(Board.class));
}
```

## 3. JaCoCo 代码覆盖率

### 3.1 运行覆盖率测试

```bash
# 运行测试并生成覆盖率报告
mvn clean test

# 只生成报告（如果测试已运行）
mvn jacoco:report

# 查看报告
open target/site/jacoco/index.html
```

### 3.2 理解覆盖率指标

JaCoCo提供以下覆盖率指标：

- **指令覆盖率（Instructions）**: 最小的代码单元覆盖率
- **分支覆盖率（Branches）**: if/switch等分支的覆盖率
- **圈复杂度（Cyclomatic Complexity）**: 代码路径的数量
- **行覆盖率（Lines）**: 源代码行的覆盖率
- **方法覆盖率（Methods）**: 方法被调用的覆盖率
- **类覆盖率（Classes）**: 类被使用的覆盖率

### 3.3 提高覆盖率的技巧

1. **测试所有公共方法**
2. **测试边界条件**
3. **测试异常情况**
4. **测试所有分支**（if/else, switch等）
5. **测试循环**（0次、1次、多次）

### 3.4 覆盖率目标

- **新项目**: 争取80%以上的行覆盖率
- **关键业务逻辑**: 争取90%以上
- **工具类/辅助类**: 可以适当降低要求
- **配置类**: 可以排除在外

## 4. PITest 变异测试

### 4.1 什么是变异测试？

变异测试通过修改代码（创建"变异体"），检验测试用例能否发现这些修改。如果测试失败，说明测试用例有效；如果测试仍然通过，说明测试用例质量不高。

### 4.2 运行变异测试

```bash
# 运行PITest
mvn org.pitest:pitest-maven:mutationCoverage

# 查看报告
open target/pit-reports/*/index.html
```

### 4.3 常见变异类型

PITest会进行以下类型的变异：

1. **条件边界变异**: `<` 改为 `<=`
2. **条件取反**: `==` 改为 `!=`
3. **算术运算变异**: `+` 改为 `-`
4. **返回值变异**: 返回true改为返回false
5. **删除方法调用**: 删除void方法调用
6. **常量变异**: 增加/减少数值常量

### 4.4 理解PITest指标

- **Line Coverage**: 行覆盖率
- **Mutation Coverage**: 变异覆盖率（被杀死的变异体比例）
- **Test Strength**: 测试强度（变异覆盖率 × 行覆盖率）

### 4.5 提高变异覆盖率

```java
// 不好的测试 - 不检查结果
@Test
void badTest() {
    calculator.add(1, 2);  // 没有断言
}

// 好的测试 - 验证结果
@Test
void goodTest() {
    int result = calculator.add(1, 2);
    assertEquals(3, result);  // 有断言，能杀死变异体
}

// 更好的测试 - 多个测试用例
@Test
void betterTest() {
    assertEquals(3, calculator.add(1, 2));
    assertEquals(0, calculator.add(-1, 1));
    assertEquals(-3, calculator.add(-1, -2));
}
```

## 5. 最佳实践

### 5.1 测试命名规范

```java
// 方法1: should_ExpectedBehavior_When_StateUnderTest
@Test
void should_ReturnSum_When_AddingTwoNumbers() {
    // ...
}

// 方法2: test + 功能描述
@Test
void testAddingTwoPositiveNumbers() {
    // ...
}

// 方法3: given_When_Then (BDD风格)
@Test
void givenTwoNumbers_WhenAdding_ThenReturnSum() {
    // ...
}
```

### 5.2 测试结构（AAA模式）

```java
@Test
void testExample() {
    // Arrange (准备) - 设置测试数据和环境
    Board board = new Board();
    Strategy strategy = new GreedyStrategy(new SumMeasure());
    
    // Act (执行) - 执行被测试的操作
    int move = strategy.move(board);
    
    // Assert (断言) - 验证结果
    assertTrue(move >= 0 && move <= 3);
}
```

### 5.3 测试独立性

```java
// 不好 - 测试之间有依赖
private static Board sharedBoard;

@Test
void test1() {
    sharedBoard = new Board();
    sharedBoard.move(Board.UP);
}

@Test
void test2() {
    // 依赖test1的执行结果
    assertNotNull(sharedBoard);
}

// 好 - 每个测试独立
@Test
void test1() {
    Board board = new Board();
    board.move(Board.UP);
    assertNotNull(board);
}

@Test
void test2() {
    Board board = new Board();
    assertNotNull(board);
}
```

### 5.4 测试边界条件

```java
@Test
void testBoundaryConditions() {
    // 测试最小值
    assertEquals(0, calculator.factorial(0));
    
    // 测试正常值
    assertEquals(120, calculator.factorial(5));
    
    // 测试负数（异常情况）
    assertThrows(IllegalArgumentException.class, 
        () -> calculator.factorial(-1));
}
```

### 5.5 使用测试数据构建器

```java
// 创建测试数据构建器
class BoardBuilder {
    private long tiles = 0;
    
    public BoardBuilder withTile(int row, int col, int value) {
        // 设置tile
        return this;
    }
    
    public Board build() {
        return new Board(tiles);
    }
}

// 在测试中使用
@Test
void testWithBuilder() {
    Board board = new BoardBuilder()
        .withTile(0, 0, 2)
        .withTile(0, 1, 4)
        .build();
    
    assertNotNull(board);
}
```

## 6. 参考资源

- [JUnit 5 官方文档](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito 官方文档](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo 文档](https://www.jacoco.org/jacoco/trunk/doc/)
- [PITest 文档](https://pitest.org/)
