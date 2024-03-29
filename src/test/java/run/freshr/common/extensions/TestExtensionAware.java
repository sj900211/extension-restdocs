package run.freshr.common.extensions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static java.util.Arrays.stream;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.util.StringUtils.hasLength;
import static run.freshr.common.security.TokenProvider.signedId;
import static run.freshr.common.security.TokenProvider.signedRole;
import static run.freshr.common.utils.ThreadUtil.threadAccess;
import static run.freshr.common.utils.ThreadUtil.threadRefresh;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import run.freshr.common.extensions.request.SearchExtension;
import run.freshr.domain.auth.enumerations.Role;

/**
 * 공통 테스트 설정 및 기능을 정의
 *
 * @author FreshR
 * @apiNote 공통 테스트 설정 및 기능을 정의
 * @since 2024. 3. 29. 오후 3:13:42
 */
@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@TestInstance(PER_CLASS)
public abstract class TestExtensionAware {

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private EntityManager entityManager;

  private MockMvc mockMvc;

  private final String DOCS_PATH = "{class-name}/{method-name}";

  @BeforeEach
  public void beforeEach(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders // MockMvc 공통 설정. 문서 출력 설정
        .webAppContextSetup(webApplicationContext)
        .addFilter(new CharacterEncodingFilter("UTF-8", true))
        .apply(documentationConfiguration(restDocumentation))
        .build();
  }

  /**
   * 데이터 반영
   *
   * @apiNote 지금까지의 영속성 컨텍스트 내용을 DB 에 반영
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public void apply() {
    entityManager.flush(); // 영속성 컨텍스트 내용을 데이터베이스에 반영
    entityManager.clear(); // 영속성 컨텍스트 초기화
  }

  /**
   * Request Header 설정
   *
   * @param mockHttpServletRequestBuilder 요청 정보 builder
   * @return header
   * @apiNote 기본적인 Request Header 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  private MockHttpServletRequestBuilder setHeader(
      MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
    return setHeader(mockHttpServletRequestBuilder, threadAccess.get());
  }

  /**
   * Request Header 설정
   *
   * @param mockHttpServletRequestBuilder mock http servlet request builder
   * @param token                         token
   * @return header
   * @apiNote Request Header 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  private MockHttpServletRequestBuilder setHeader(
      MockHttpServletRequestBuilder mockHttpServletRequestBuilder, String token) {
    if (hasLength(token)) {
      mockHttpServletRequestBuilder.header("Authorization", "Bearer " + token);
    }

    return mockHttpServletRequestBuilder
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON);
  }

  /**
   * Multipart Request Header 설정
   *
   * @param mockHttpServletRequestBuilder 요청 정보 builder
   * @return multipart
   * @apiNote multipart/form-data 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  private MockHttpServletRequestBuilder setMultipartHeader(
      MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
    return setMultipartHeader(mockHttpServletRequestBuilder, threadAccess.get());
  }

  /**
   * Multipart Request Header 설정
   *
   * @param mockHttpServletRequestBuilder 요청 정보 builder
   * @return multipart
   * @apiNote multipart/form-data 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  private MockHttpServletRequestBuilder setMultipartHeader(
      MockHttpServletRequestBuilder mockHttpServletRequestBuilder, String token) {
    if (hasLength(token)) {
      mockHttpServletRequestBuilder.header("Authorization", "Bearer " + token);
    }

    return mockHttpServletRequestBuilder
        .contentType(MULTIPART_FORM_DATA)
        .accept(APPLICATION_JSON);
  }

  /**
   * GET 통신
   *
   * @param uri           URI 정보
   * @param pathVariables path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Path Parameter 외에 설정한 정보가 없는 통신
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public ResultActions GET(String uri, Object... pathVariables) throws Exception {
    return GET_PARAM(uri, null, pathVariables);
  }

  /**
   * GET 통신
   *
   * @param <T>           요청 query string 데이터 유형
   * @param uri           URI 정보
   * @param search        요청 query string 데이터
   * @param pathVariables path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Request Parameter 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public <T extends SearchExtension<?>> ResultActions GET_PARAM(String uri, T search,
      Object... pathVariables) throws Exception {
    MockHttpServletRequestBuilder header = get(uri, pathVariables);

    if (!isNull(search)) {
      stream(search.getClass().getDeclaredFields()).forEach(field -> {
        try {
          field.setAccessible(true);

          if (!isNull(field.get(search))) {
            if (!field.getType().equals(List.class)) {
              header.param(field.getName(), field.get(search).toString());
            } else {
              String valueString = field.get(search).toString();

              valueString = valueString.substring(1, valueString.length() - 1);

              List<String> valueList = of(valueString.split(", "));
              int max = valueList.size();

              for (int i = 0; i < max; i++) {
                header.param(field.getName() + "[" + i + "]", valueList.get(i));
              }
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      });

      stream(search.getClass().getSuperclass().getDeclaredFields()).forEach(field -> {
        try {
          field.setAccessible(true);

          if (!isNull(field.get(search))) {
            if (!field.getType().equals(List.class)) {
              header.param(field.getName(), field.get(search).toString());
            } else {
              String valueString = field.get(search).toString();

              valueString = valueString.substring(1, valueString.length() - 1);

              List<String> valueList = of(valueString.split(", "));
              int max = valueList.size();

              for (int i = 0; i < max; i++) {
                header
                    .param(field.getName() + "[" + i + "]", valueList.get(i));
              }
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      });
    }

    return mockMvc.perform(setHeader(header));
  }

  /**
   * POST 통신
   *
   * @param uri           URI 정보
   * @param pathVariables path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Path Parameter 외에 설정한 정보가 없는 통신
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public ResultActions POST(String uri, Object... pathVariables) throws Exception {
    return POST_BODY(uri, null, pathVariables);
  }

  /**
   * POST 통신
   *
   * @param <T>           요청 body 데이터 유형
   * @param uri           URI 정보
   * @param content       요청 body 데이터
   * @param pathVariables path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Request Body 를 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public <T> ResultActions POST_BODY(String uri, T content, Object... pathVariables)
      throws Exception {
    MockHttpServletRequestBuilder header = setHeader(post(uri, pathVariables));

    if (!isNull(content)) {
      header.content(objectMapper.writeValueAsString(content));
    }

    return mockMvc.perform(header);
  }

  /**
   * POST 통신
   *
   * @param uri           URI 정보
   * @param token         설정할 토큰 정보
   * @param pathVariables path variables
   * @return result actions
   * @throws Exception exception
   * @apiNote Authorization 에 JWT 토큰을 따로 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public ResultActions POST_TOKEN(String uri, String token, Object... pathVariables)
      throws Exception {
    return POST_TOKEN_BODY(uri, token, null, pathVariables);
  }

  /**
   * POST 통신
   *
   * @param <T>           type parameter
   * @param uri           URI 정보
   * @param token         설정할 토큰 정보
   * @param content       요청 body 데이터
   * @param pathVariables path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Authorization 에 JWT 토큰을 따로 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public <T> ResultActions POST_TOKEN_BODY(String uri, String token, T content,
      Object... pathVariables)
      throws Exception {
    MockHttpServletRequestBuilder header = setHeader(post(uri, pathVariables), token);

    if (!isNull(content)) {
      header.content(objectMapper.writeValueAsString(content));
    }

    return mockMvc.perform(header);
  }

  /**
   * POST 통신
   *
   * @param uri               URI 정보
   * @param directory         저장할 directory 이름
   * @param mockMultipartFile 파일 데이터
   * @param pathVariables     path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Multipart 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public ResultActions POST_MULTIPART(String uri, String directory,
      MockMultipartFile mockMultipartFile, Object... pathVariables) throws Exception {
    MockMultipartHttpServletRequestBuilder file = multipart(uri, pathVariables)
        .file(mockMultipartFile);

    if (hasLength(directory)) {
      file.param("directory", directory);
    }

    return mockMvc.perform(setMultipartHeader(file));
  }

  /**
   * PUT 통신
   *
   * @param uri           URI 정보
   * @param pathVariables path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Path Parameter 외에 설정한 정보가 없는 통신
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public ResultActions PUT(String uri, Object... pathVariables) throws Exception {
    return PUT_BODY(uri, null, pathVariables);
  }

  /**
   * PUT 통신
   *
   * @param <T>           요청 body 데이터 유형
   * @param uri           URI 정보
   * @param content       요청 body 데이터
   * @param pathVariables path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Request Body 를 설정
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public <T> ResultActions PUT_BODY(String uri, T content, Object... pathVariables)
      throws Exception {
    MockHttpServletRequestBuilder header = setHeader(put(uri, pathVariables));

    if (!isNull(content)) {
      header.content(objectMapper.writeValueAsString(content));
    }

    return mockMvc.perform(header);
  }

  /**
   * DELETE 통신
   *
   * @param uri           URI 정보
   * @param pathVariables path parameter 값 목록
   * @return result actions
   * @throws Exception exception
   * @apiNote Path Parameter 외에 설정한 정보가 없는 통신
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public ResultActions DELETE(String uri, Object... pathVariables) throws Exception {
    return mockMvc.perform(setHeader(delete(uri, pathVariables)));
  }

  /**
   * Document 작성
   *
   * @param snippets 문서 구성 요소
   * @return rest documentation result handler
   * @apiNote 코드를 조금이라도 짧게 만들고 싶어서 만든 기능...⭐
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public RestDocumentationResultHandler docs(ResourceSnippetParameters snippets) {
    return MockMvcRestDocumentationWrapper.document(DOCS_PATH, preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()), resource(snippets));
  }

  /**
   * 계정 일련 번호 조회
   *
   * @return signed id
   * @apiNote 통신중인 계정의 일련 번호 조회
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public String getSignedId() {
    return signedId.get();
  }

  /**
   * 권한 조회
   *
   * @return signed role
   * @apiNote 통신중인 계정의 권한 조회
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public Role getSignedRole() {
    return signedRole.get();
  }

  /**
   * 인증 정보 제거
   *
   * @apiNote 통신중인 인증 정보 제거
   * @author FreshR
   * @since 2024. 3. 29. 오후 3:13:42
   */
  public void removeSigned() {
    signedRole.remove();
    signedId.remove();
    threadAccess.remove();
    threadRefresh.remove();
  }

}
