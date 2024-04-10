package run.freshr.common.utils;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.util.StringUtils.hasLength;
import static run.freshr.common.enumerations.ColumnType.BIGINT;
import static run.freshr.common.enumerations.ColumnType.BIT;
import static run.freshr.common.enumerations.ColumnType.BLOB;
import static run.freshr.common.enumerations.ColumnType.DATE;
import static run.freshr.common.enumerations.ColumnType.DATETIME;
import static run.freshr.common.enumerations.ColumnType.DECIMAL;
import static run.freshr.common.enumerations.ColumnType.DOUBLE;
import static run.freshr.common.enumerations.ColumnType.FLOAT;
import static run.freshr.common.enumerations.ColumnType.INT;
import static run.freshr.common.enumerations.ColumnType.SMALLINT;
import static run.freshr.common.enumerations.ColumnType.TIME;
import static run.freshr.common.enumerations.ColumnType.TINYINT;
import static run.freshr.common.enumerations.ColumnType.UNKNOWN;
import static run.freshr.common.enumerations.ColumnType.VARCHAR;
import static run.freshr.common.enumerations.FieldType.BOOLEAN;
import static run.freshr.common.enumerations.FieldType.ENUM;
import static run.freshr.common.enumerations.FieldType.NUMBER;
import static run.freshr.common.enumerations.FieldType.STRING;

import com.querydsl.core.types.Path;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.restdocs.snippet.Attributes.Attribute;
import run.freshr.common.data.SearchData;
import run.freshr.common.enumerations.ColumnType;
import run.freshr.common.enumerations.FieldType;
import run.freshr.common.mappers.EnumModel;

/**
 * 문서 편의 기능 정의.
 *
 * @author FreshR
 * @apiNote RestDocs 문서 편의 기능 정의
 * @since 2024. 3. 29. 오후 2:00:15
 */
public class PrintUtil {

  /**
   * {@link ParameterDescriptor} 목록
   *
   * @apiNote {@link ParameterDescriptor} 목록
   * @since 2024. 3. 29. 오후 2:00:15
   */
  @Getter
  private final List<ParameterDescriptor> parameterList = new ArrayList<>();
  /**
   * {@link FieldDescriptor} 목록
   *
   * @apiNote {@link FieldDescriptor} 목록
   * @since 2024. 3. 29. 오후 2:00:15
   */
  @Getter
  private final List<FieldDescriptor> fieldList = new ArrayList<>();

  /**
   * 생성자
   *
   * @apiNote 생성자
   * @author FreshR
   * @since 2024. 3. 29. 오후 2:00:15
   */
  public PrintUtil() {
  }

  /**
   * 생성자
   *
   * @param builder builder
   * @apiNote {@link Builder} 생성자
   * @author FreshR
   * @since 2024. 3. 29. 오후 2:00:15
   */
  public PrintUtil(Builder builder) {
    this.parameterList.addAll(builder.parameterList);
    this.fieldList.addAll(builder.fieldList);
  }

  /**
   * 세부항목 builder 반환
   *
   * @return builder
   * @apiNote 세부항목 builder 반환
   * @author FreshR
   * @since 2024. 3. 29. 오후 2:00:15
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * {@link ParameterDescriptor} 목록 반환
   *
   * @return parameter descriptor [ ]
   * @apiNote {@link ParameterDescriptor} 목록 반환
   * @author FreshR
   * @since 2024. 3. 29. 오후 2:00:15
   */
  public ParameterDescriptor[] getParameters() {
    return parameterList.toArray(new ParameterDescriptor[0]);
  }

  /**
   * 열거형 데이터 문서화
   *
   * @param <E>          type parameter
   * @param enumerations enumerations
   * @return attribute
   * @apiNote 열거형 데이터 문서화
   * @author FreshR
   * @since 2024. 3. 29. 오후 2:00:15
   */
  public static <E extends EnumModel> Attribute enumValues(E[] enumerations) {
    List<String> values = Arrays.stream(enumerations)
        .map(EnumModel::getKey)
        .toList();

    return Attributes.key("enumValues").value(values);
  }

  // .______    __    __   __   __       _______   _______ .______
  // |   _  \  |  |  |  | |  | |  |     |       \ |   ____||   _  \
  // |  |_)  | |  |  |  | |  | |  |     |  .--.  ||  |__   |  |_)  |
  // |   _  <  |  |  |  | |  | |  |     |  |  |  ||   __|  |      /
  // |  |_)  | |  `--'  | |  | |  `----.|  '--'  ||  |____ |  |\  \----.
  // |______/   \______/  |__| |_______||_______/ |_______|| _| `._____|

  /**
   * 세부항목 builder
   *
   * @author FreshR
   * @apiNote 세부항목 builder
   * @since 2024. 3. 29. 오후 2:00:15
   */
  public static class Builder {

    /**
     * {@link ParameterDescriptor} 목록
     *
     * @apiNote {@link ParameterDescriptor} 목록
     * @since 2024. 3. 29. 오후 2:00:15
     */
    private final List<ParameterDescriptor> parameterList = new ArrayList<>();
    /**
     * {@link FieldDescriptor} 목록
     *
     * @apiNote {@link FieldDescriptor} 목록
     * @since 2024. 3. 29. 오후 2:00:15
     */
    private final List<FieldDescriptor> fieldList = new ArrayList<>();
    /**
     * prefix 경로 변수
     *
     * @apiNote parameter name & field path 의 prefix 설정<br>
     * 설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @since 2024. 3. 29. 오후 2:00:15
     */
    private String prefix = "";
    /**
     * 설명 prefix 변수
     *
     * @apiNote description 의 prefix 설정<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @since 2024. 3. 29. 오후 2:00:15
     */
    private String prefixDescription = "";
    /**
     * 선택 입력 여부 변수 및 초기값 설정
     *
     * @apiNote 필수 항목 설정<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @since 2024. 3. 29. 오후 2:00:15
     */
    private Boolean prefixOptional = false;
    /**
     * 설명 Size 정보 추가 여부 변수 및 초기값 설정
     *
     * @apiNote 설명 Size 정보 추가 여부 설정<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @since 2024. 3. 29. 오후 2:00:15
     */
    private Boolean isSize = false;

    /**
     * 생성자
     *
     * @apiNote 생성자
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder() {
    }

    /**
     * 객체 build
     *
     * @return print util
     * @apiNote 객체 build
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public PrintUtil build() {
      return new PrintUtil(this);
    }

    /**
     * parameter name & field path 의 prefix 설정
     *
     * @param prefix prefix 경로
     * @return builder
     * @apiNote parameter name & field path 의 prefix 설정<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder prefix(String prefix) {
      this.prefix = prefix;

      return this;
    }

    /**
     * description 의 prefix 설정
     *
     * @param prefixDescription 설명 prefix 문자
     * @return builder
     * @apiNote description 의 prefix 설정<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder prefixDescription(String prefixDescription) {
      this.prefixDescription = prefixDescription;

      return this;
    }

    /**
     * 필수 항목 설정
     *
     * @return builder
     * @apiNote 필수 항목 설정<br>
     *          TRUE 로 설정 된다.<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder prefixOptional() {
      return prefixOptional(true);
    }

    /**
     * 필수 항목 설정
     *
     * @param optional 선택 입력 여부
     * @return builder
     * @apiNote 필수 항목 설정<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder prefixOptional(Boolean optional) {
      this.prefixOptional = optional;

      return this;
    }

    /**
     * parameter name & field path 의 prefix 설정 제거
     *
     * @return builder
     * @apiNote parameter name & field path 의 prefix 설정 제거<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder clearPrefix() {
      this.prefix = "";

      return this;
    }

    /**
     * description 의 prefix 설정 제거
     *
     * @return builder
     * @apiNote description 의 prefix 설정 제거<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder clearPrefixDescription() {
      this.prefixDescription = "";

      return this;
    }

    /**
     * 필수 항목 설정 제거
     *
     * @return builder
     * @apiNote 필수 항목 설정 제거<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder clearOptional() {
      this.prefixOptional = false;

      return this;
    }

    /**
     * 설명 Size 정보 추가 여부 설정 제거
     *
     * @return builder
     * @apiNote 설명 Size 정보 추가 여부 설정 제거<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder clearIsSize() {
      this.isSize = false;

      return this;
    }

    /**
     * 모든 Prefix 설정 제거
     *
     * @return builder
     * @apiNote 모든 Prefix 설정 제거<br>
     *          설정한 이후 다시 설정하기 전까지는 모든 항목에 적용된다.
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder clear() {
      this.prefix = "";
      this.prefixDescription = "";
      this.prefixOptional = false;
      this.isSize = false;

      return this;
    }

    // .______      ___      .______          ___      .___  ___.  _______ .___________. _______ .______
    // |   _  \    /   \     |   _  \        /   \     |   \/   | |   ____||           ||   ____||   _  \
    // |  |_)  |  /  ^  \    |  |_)  |      /  ^  \    |  \  /  | |  |__   `---|  |----`|  |__   |  |_)  |
    // |   ___/  /  /_\  \   |      /      /  /_\  \   |  |\/|  | |   __|      |  |     |   __|  |      /
    // |  |     /  _____  \  |  |\  \----./  _____  \  |  |  |  | |  |____     |  |     |  |____ |  |\  \----.
    // | _|    /__/     \__\ | _| `._____/__/     \__\ |__|  |__| |_______|    |__|     |_______|| _| `._____|

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param paths paths
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?>... paths) {
      List.of(paths).forEach(this::parameter);

      return this;
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param path path
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?> path) {
      return parameter(path, null, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?> path, String description) {
      return parameter(path, description, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param path     path
     * @param optional optional
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?> path, Boolean optional) {
      return parameter(path, null, optional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param path       path
     * @param attributes attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?> path, Attribute... attributes) {
      return parameter(path, null, prefixOptional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @param optional    optional
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?> path, String description, Boolean optional) {
      return parameter(path, description, optional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?> path, String description, Attribute... attributes) {
      return parameter(path, description, prefixOptional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param path       path
     * @param optional   optional
     * @param attributes attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?> path, Boolean optional, Attribute... attributes) {
      return parameter(path, null, optional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param path              path
     * @param customDescription custom description
     * @param optional          optional
     * @param attributes        attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(Path<?> path, String customDescription, Boolean optional,
        Attribute... attributes) {
      HashMap<String, Object> map = pathMap(path);
      String name = map.get("name").toString();
      String description = map.get("description").toString();
      String parameterDescription = hasLength(customDescription) ? customDescription : description;
      String size = ofNullable(map.get("size")).orElse("").toString();
      String format = ofNullable(map.get("format")).orElse("").toString();

      if (hasLength(format)) {
        parameterDescription += " | format: " + format;
      }

      if (hasLength(size) && isSize) {
        parameterDescription += " | size: " + size;
      }

      return parameter(name, parameterDescription, optional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docDatas docDatas
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(HashMap<?, ?>... docDatas) {
      List.of(docDatas).forEach(this::parameter);

      return this;
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param array array
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 4. 4. 오전 10:26:22
     */
    public Builder parameter(SearchData... array) {
      List.of(array).forEach(this::parameter);

      return this;
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docData docData
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(SearchData docData) {
      return parameter(docData, null, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docData     docData
     * @param description description
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(SearchData docData, String description) {
      return parameter(docData, description, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docData  docData
     * @param optional optional
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(SearchData docData, Boolean optional) {
      return parameter(docData, null, optional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docData    docData
     * @param attributes attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(SearchData docData, Attribute... attributes) {
      return parameter(docData, null, prefixOptional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docData     docData
     * @param description description
     * @param optional    optional
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(SearchData docData, String description, Boolean optional) {
      return parameter(docData, description, optional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docData     docData
     * @param description description
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(SearchData docData, String description, Attribute... attributes) {
      return parameter(docData, description, prefixOptional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docData    docData
     * @param optional   optional
     * @param attributes attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(SearchData docData, Boolean optional, Attribute... attributes) {
      return parameter(docData, null, optional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param docData     docData
     * @param description description
     * @param optional    optional
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(SearchData docData, String description, Boolean optional,
        Attribute... attributes) {
      String name = docData.getName();
      String comment = docData.getComment();
      String format = docData.getFormat();
      String parameterDescription = hasLength(description) ? description : comment;

      if (hasLength(format)) {
        parameterDescription += " | format: " + format;
      }

      return parameter(name, parameterDescription, optional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param name        name
     * @param description description
     * @param optional    optional
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(String name, String description, Boolean optional) {
      return parameter(name, description, optional, new Attribute[]{});
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param name        name
     * @param description description
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(String name, String description, Attribute... attributes) {
      return parameter(name, description, prefixOptional, attributes);
    }

    /**
     * {@link ParameterDescriptor} 생성
     *
     * @param name        name
     * @param description description
     * @param optional    optional
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link ParameterDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameter(String name, String description, Boolean optional,
        Attribute... attributes) {
      String parameterName = (hasLength(prefix) ? prefix + "." : "") + name;
      ParameterDescriptor parameterDescriptor = parameterWithName(parameterName)
          .description(prefixDescription + " " + description)
          .attributes(attributes);

      if (optional) {
        parameterDescriptor.optional();
      }

      parameterList.add(parameterDescriptor);

      return this;
    }

    /**
     * {@link ParameterDescriptor} 목록을 Builder 에 추가
     *
     * @param parameterDescriptors parameter descriptors
     * @return builder
     * @apiNote {@link ParameterDescriptor} 목록을 Builder 에 추가
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder parameters(List<ParameterDescriptor> parameterDescriptors) {
      parameterList.addAll(parameterDescriptors);

      return this;
    }

    //  _______  __   _______  __       _______
    // |   ____||  | |   ____||  |     |       \
    // |  |__   |  | |  |__   |  |     |  .--.  |
    // |   __|  |  | |   __|  |  |     |  |  |  |
    // |  |     |  | |  |____ |  `----.|  '--'  |
    // |__|     |__| |_______||_______||_______/

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param paths paths
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?>... paths) {
      List.of(paths).forEach(this::field);

      return this;
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path path
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path) {
      return field(path, null, null, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, String description) {
      return field(path, description, null, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path path
     * @param type type
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, FieldType type) {
      return field(path, null, type, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path     path
     * @param optional optional
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, Boolean optional) {
      return field(path, null, null, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path       path
     * @param attributes attributes
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, Attribute... attributes) {
      return field(path, null, null, prefixOptional, attributes);
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @param type        type
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, String description, FieldType type) {
      return field(path, description, type, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @param optional    optional
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, String description, Boolean optional) {
      return field(path, description, null, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, String description, Attribute... attributes) {
      return field(path, description, null, prefixOptional, attributes);
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @param type        type
     * @param optional    optional
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, String description, FieldType type, Boolean optional) {
      return field(path, description, type, prefixOptional, new Attribute[]{});
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @param type        type
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, String description, FieldType type,
        Attribute... attributes) {
      return field(path, description, type, prefixOptional, attributes);
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param path        path
     * @param description description
     * @param type        type
     * @param optional    optional
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(Path<?> path, String description, FieldType type, Boolean optional,
        Attribute... attributes) {
      HashMap<String, Object> map = pathMap(path);
      String name = map.get("name").toString();
      String fieldDescription = hasLength(description) ? description
          : map.get("description").toString();
      Object fieldType = !isNull(type) ? type : map.get("type");

      return field(name, fieldDescription, fieldType, optional, attributes);
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param name        name
     * @param description description
     * @param type        type
     * @param optional    optional
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(String name, String description, Object type, Boolean optional) {
      return field(name, description, type, optional, new Attribute[]{});
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param name        name
     * @param description description
     * @param type        type
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(String name, String description, Object type, Attribute... attributes) {
      return field(name, description, type, prefixOptional, attributes);
    }

    /**
     * {@link FieldDescriptor} 생성
     *
     * @param name        name
     * @param description description
     * @param type        type
     * @param optional    optional
     * @param attributes  attributes
     * @return builder
     * @apiNote {@link FieldDescriptor} 생성
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder field(String name, String description, Object type, Boolean optional,
        Attribute... attributes) {
      String fieldName = (hasLength(prefix) ? prefix + "." : "") + name;
      FieldDescriptor fieldDescriptor = fieldWithPath(fieldName)
          .type(type)
          .description(description)
          .attributes(attributes);

      if (optional) {
        fieldDescriptor.optional();
      }

      fieldList.add(fieldDescriptor);

      return this;
    }

    /**
     * {@link FieldDescriptor} 목록을 Builder 에 추가
     *
     * @param fieldDescriptors field descriptors
     * @return builder
     * @apiNote {@link FieldDescriptor} 목록을 Builder 에 추가
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public Builder fields(List<FieldDescriptor> fieldDescriptors) {
      fieldList.addAll(fieldDescriptors);

      return this;
    }

    //   ______   ______   .___  ___. .___  ___.   ______   .__   __.
    //  /      | /  __  \  |   \/   | |   \/   |  /  __  \  |  \ |  |
    // |  ,----'|  |  |  | |  \  /  | |  \  /  | |  |  |  | |   \|  |
    // |  |     |  |  |  | |  |\/|  | |  |\/|  | |  |  |  | |  . `  |
    // |  `----.|  `--'  | |  |  |  | |  |  |  | |  `--'  | |  |\   |
    //  \______| \______/  |__|  |__| |__|  |__|  \______/  |__| \__|

    /**
     * {@link Path} 정보를 {@link HashMap} 으로 변환
     *
     * @param path QueryDsl path 데이터
     * @return hash map
     * @apiNote {@link Path} 정보를 {@link HashMap} 으로 변환
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    public HashMap<String, Object> pathMap(Path<?> path) {
      HashMap<String, Object> map = new HashMap<>();
      String qPath = path.toString();
      int qDotPoint = qPath.indexOf(".") + 1;
      String target = qPath.substring(0, qDotPoint);
      String name = qPath.replace(target, "").replace(")", "[]");
      ColumnType columnType = getColumnType(path.getType().getTypeName());
      String description = "";
      String size = columnType.getSize();
      String format = columnType.getFormat();
      FieldType type = getJsonType(columnType);

      Comment comment = path.getAnnotatedElement().getAnnotation(Comment.class);
      Column column = path.getAnnotatedElement().getAnnotation(Column.class);
      Enumerated enumerated = path.getAnnotatedElement().getAnnotation(Enumerated.class);

      if (!isNull(comment)) {
        description = comment.value();
      }

      if (!isNull(enumerated)) {
        type = ENUM;
      }

      if (!isNull(column) && path.getType().getTypeName().equals(String.class.getTypeName())) {
        size = Optional.of(column.length()).orElse(0) + " characters";
      }

      map.put("name", name); // 이름
      map.put("description", description); // 설명
      map.put("columnType", columnType); // 컬럼 유형
      map.put("size", size); // 제한 크기
      map.put("format", format); // 규칙
      map.put("type", type); // 유형

      return map;
    }

    /**
     * 컬럼 유형 조회
     *
     * @param type 데이터 유형
     * @return column type
     * @apiNote 데이터 유형으로 Database 데이터 유형 조회
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    private ColumnType getColumnType(String type) {
      return switch (type) {
        case "java.lang.Float" -> FLOAT;
        case "java.lang.Double" -> DOUBLE;
        case "java.lang.BigDecimal" -> DECIMAL;
        case "java.lang.Byte" -> TINYINT;
        case "java.lang.Short" -> SMALLINT;
        case "java.lang.String" -> VARCHAR;
        case "java.lang.Long" -> BIGINT;
        case "java.lang.Integer" -> INT;
        case "java.lang.Boolean" -> BIT;
        case "java.time.LocalDate" -> DATE;
        case "java.time.LocalDateTime" -> DATETIME;
        case "java.time.LocalTime" -> TIME;
        case "byte[]" -> BLOB;
        default -> UNKNOWN;
      };
    }

    /**
     * Json 유형 조회
     *
     * @param columnType 컬럼 데이터 유형
     * @return json type
     * @apiNote 데이터 유형으로 Json 데이터 유형 조회
     * @author FreshR
     * @since 2024. 3. 29. 오후 2:00:15
     */
    private FieldType getJsonType(ColumnType columnType) {
      return switch (columnType) {
        case TINYINT, BIT -> BOOLEAN;
        case FLOAT, DOUBLE, DECIMAL, SMALLINT, BIGINT, INT -> NUMBER;
        default -> STRING;
      };
    }
  }

}
