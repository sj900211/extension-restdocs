package run.freshr.common.extensions;

import static run.freshr.common.security.TokenProvider.signedId;
import static run.freshr.common.security.TokenProvider.signedRole;
import static run.freshr.domain.auth.enumerations.Role.ROLE_ANONYMOUS;
import static run.freshr.domain.auth.enumerations.Role.ROLE_MANAGER_MAJOR;
import static run.freshr.domain.auth.enumerations.Role.ROLE_MANAGER_MINOR;
import static run.freshr.domain.auth.enumerations.Role.ROLE_USER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import run.freshr.domain.auth.enumerations.Role;
import run.freshr.service.TestSecurityServiceAware;

/**
 * 공통 보안 기능 정의
 *
 * @author FreshR
 * @apiNote 공통 보안 기능 정의
 * @since 2024. 3. 29. 오후 3:13:42
 */
public abstract class TestSecurityExtensionAware
    <S extends TestSecurityServiceAware, R extends TestSecurityRunnerAware>
    extends TestExtensionAware {

  @Autowired
  protected S service;

  /**
   * 인증 정보 설정
   *
   * @param id   일련 번호
   * @param role 권한
   * @apiNote 인증 정보 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  private void authentication(String id, Role role) {
    removeSigned(); // 로그아웃 처리

    if (!role.equals(ROLE_ANONYMOUS)) { // 게스트 권한이 아닐 경우
      service.createAuth(id, role); // 토큰 발급 및 등록
    }

    signedRole.set(role); // 로그인한 계정 권한 설정
    signedId.set(id); // 로그인한 계정 일련 번호 설정

    SecurityContextHolder // 일회용 로그인 설정
        .getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(
            role.getPrivilege(),
            "{noop}",
            AuthorityUtils.createAuthorityList(role.getKey())
        ));
  }

  /**
   * 인증 정보 생성
   *
   * @apiNote ROLE_USER 권한으로 인증 정보 생성
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public void setSignedUser() {
    authentication(R.userId, ROLE_USER);
  }

  /**
   * 인증 정보 생성
   *
   * @apiNote ROLE_MANAGER_MINOR 권한으로 인증 정보 생성
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public void setSignedManager() {
    authentication(R.managerId, ROLE_MANAGER_MINOR);
  }

  /**
   * 인증 정보 생성
   *
   * @apiNote ROLE_MANAGER_MAJOR 권한으로 인증 정보 생성
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public void setSignedMighty() {
    authentication(R.mightyId, ROLE_MANAGER_MAJOR);
  }

  /**
   * 인증 정보 생성
   *
   * @apiNote ROLE_ANONYMOUS 권한으로 인증 정보 생성
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public void setAnonymous() {
    authentication("", ROLE_ANONYMOUS);
  }

  /**
   * RSA 정보 생성
   *
   * @apiNote RSA 정보 생성
   * @author FreshR
   * @since 2024. 4. 2. 오후 3:38:05
   */
  protected void setRsa() {
    service.createRsa();
  }

}
