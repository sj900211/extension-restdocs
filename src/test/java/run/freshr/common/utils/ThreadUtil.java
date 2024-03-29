package run.freshr.common.utils;

/**
 * 테스트 전역 변수 관리
 *
 * @author FreshR
 * @apiNote 테스트를 실행 중에 사용할 전역 변수 관리
 * @since 2024. 3. 29. 오후 2:03:28
 */
public class ThreadUtil {

  public static ThreadLocal<String> threadAccess = new ThreadLocal<>(); // ACCESS TOKEN
  public static ThreadLocal<String> threadRefresh = new ThreadLocal<>(); // REFRESH TOKEN

}
