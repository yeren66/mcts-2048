# Maven测试常用命令速查表

## 基础命令

### 编译
```bash
mvn compile                    # 编译主代码
mvn test-compile              # 编译测试代码
mvn clean compile             # 清理后重新编译
```

### 测试
```bash
mvn test                      # 运行所有测试
mvn test -Dtest=BoardTest     # 运行指定测试类
mvn test -Dtest=BoardTest#testEquals  # 运行指定测试方法
mvn test -Dtest=Board*        # 运行所有Board开头的测试类
```

### 打包
```bash
mvn package                   # 打包（会先运行测试）
mvn package -DskipTests       # 打包但跳过测试
mvn clean package             # 清理后重新打包
```

### 运行
```bash
# 使用Maven运行
mvn exec:java -Dexec.mainClass="dk.ahle.thomas.mcts2048.Main"

# 或者运行打包后的JAR
java -jar target/mcts-2048-1.0.0.jar
```

## JaCoCo代码覆盖率

### 生成覆盖率报告
```bash
mvn clean test                # 运行测试（自动生成覆盖率）
mvn jacoco:report             # 单独生成报告
```

### 查看报告
```bash
# macOS
open target/site/jacoco/index.html

# Linux
xdg-open target/site/jacoco/index.html

# Windows
start target/site/jacoco/index.html
```
> 也可直接鼠标双击打开 `target/site/jacoco/index.html` 文件查看。

### 报告位置
- HTML报告: `target/site/jacoco/index.html`
- XML报告: `target/site/jacoco/jacoco.xml`
- CSV报告: `target/site/jacoco/jacoco.csv`

### 设置覆盖率阈值
在pom.xml中配置最低覆盖率要求，构建时会检查。

## PITest变异测试

### 运行变异测试
```bash
mvn org.pitest:pitest-maven:mutationCoverage
```

### 加速执行
```bash
# 使用多线程（4个线程）
mvn org.pitest:pitest-maven:mutationCoverage -Dthreads=4

# 使用历史数据加速
mvn org.pitest:pitest-maven:mutationCoverage -DwithHistory
```

### 只测试特定类
```bash
mvn org.pitest:pitest-maven:mutationCoverage \
  -DtargetClasses=dk.ahle.thomas.mcts2048.Board
```

### 查看报告
```bash
# macOS
open target/pit-reports/*/index.html

# Linux
xdg-open target/pit-reports/*/index.html
```

### 报告位置
- HTML报告: `target/pit-reports/YYYYMMDDHHMMSS/index.html`

## 组合命令

### 完整验证流程
```bash
# 清理、编译、测试、打包
mvn clean verify

# 包含覆盖率检查
mvn clean test jacoco:report jacoco:check
```

### 生成所有报告
```bash
# 1. 运行测试和覆盖率
mvn clean test jacoco:report

# 2. 运行变异测试
mvn org.pitest:pitest-maven:mutationCoverage

# 3. 打开报告
open target/site/jacoco/index.html
open target/pit-reports/*/index.html
```

## 调试相关

### 查看详细日志
```bash
mvn test -X                   # 显示DEBUG级别日志
mvn test -e                   # 显示错误堆栈
```

### 只运行失败的测试
```bash
mvn test -Dsurefire.rerunFailingTestsCount=2
```

### 跳过测试
```bash
mvn package -DskipTests       # 跳过测试运行
mvn package -Dmaven.test.skip=true  # 跳过测试编译和运行
```

## 依赖管理

### 查看依赖树
```bash
mvn dependency:tree
```

### 查看可用的插件目标
```bash
mvn help:describe -Dplugin=org.jacoco:jacoco-maven-plugin
mvn help:describe -Dplugin=org.pitest:pitest-maven
```

### 更新依赖
```bash
mvn versions:display-dependency-updates
```

## IDE集成（可选）

### IntelliJ IDEA
1. File -> Open -> 选择pom.xml
2. 右键项目 -> Add as Maven Project
3. 运行配置: Run -> Edit Configurations -> Add New -> JUnit

### Eclipse
1. File -> Import -> Maven -> Existing Maven Projects
2. 选择项目目录
3. 右键项目 -> Run As -> JUnit Test

### VS Code
1. 安装Java Extension Pack
2. 安装Maven for Java扩展
3. 侧边栏会显示Maven视图和测试资源管理器

## 常见问题解决

### 问题1: 测试找不到
**原因**: 测试类没有放在正确的位置
**解决**: 确保测试类在 `src/test/java` 目录下

### 问题2: JaCoCo报告为空
**原因**: 没有运行测试
**解决**: `mvn clean test jacoco:report`

### 问题3: PITest运行太慢
**解决**: 使用 `-Dthreads=4 -DwithHistory`

### 问题4: 依赖冲突
**解决**: 
```bash
mvn dependency:tree
mvn dependency:analyze
```