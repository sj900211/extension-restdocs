package run.freshr.common.extensions;

import org.springframework.boot.ApplicationRunner;

/**
 * Test runner 추상 클래스
 *
 * @author FreshR
 * @apiNote Application Run 마지막에 동작하는 Class<br>
 *          Test 코드가 실행되기 전에 동작하도록 구성<br>
 *          보안 기능을 사용하는 서비스인 경우  {@link TestSecurityExtensionAware} 와 함께 편의 기능 제공
 * @since 2024. 3. 29. 오후 3:13:42
 */
public abstract class TestSecurityRunnerAware implements ApplicationRunner {

  public static String userId;
  public static String managerId;
  public static String mightyId;

}
