package run.freshr.service;

import org.springframework.boot.ApplicationRunner;
import run.freshr.domain.auth.enumerations.Role;

/**
 * 테스트 데이터 관리 service 공통 보안 기능
 *
 * @author FreshR
 * @apiNote {@link ApplicationRunner} 를 상속받은 test runner 에서<br>
 *          데이터 설정을 쉽게하기 위해서 공통 데이터 생성 기능을 재정의<br>
 *          필수 작성은 아니며, 테스트 코드에서 데이터 생성 기능을 조금이라도 더 편하게 사용하고자 만든 Service<br>
 *          권한과 같은 특수한 경우를 제외한 대부분은 데이터에 대한 Create, Get 정도만 작성해서 사용을 한다.
 * @since 2024. 3. 29. 오후 3:10:48
 */
public interface TestSecurityServiceAware {

  //      ___      __    __  .___________. __    __
  //     /   \    |  |  |  | |           ||  |  |  |
  //    /  ^  \   |  |  |  | `---|  |----`|  |__|  |
  //   /  /_\  \  |  |  |  |     |  |     |   __   |
  //  /  _____  \ |  `--'  |     |  |     |  |  |  |
  // /__/     \__\ \______/      |__|     |__|  |__|

  void createAuth(String id, Role role);

}
