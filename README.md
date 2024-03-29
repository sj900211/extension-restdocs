# Extension > RestDocs
> 테스트 코드 작성과 문서 자동화를 위한 기능 정의  
> 이 모듈은 Spring Rest Docs 와 [Rest Docs API Spec](https://github.com/ePages-de/restdocs-api-spec) 기능을 사용한다.  
> 

> - ## [TestExtensionAware](./src/test/java/run/freshr/common/extensions/TestExtensionAware.java)
>> 테스트 코드 작성에서 반복되는 코드를 정의
> 
> - ## [TestSecurityExtensionAware](./src/test/java/run/freshr/common/extensions/TestSecurityExtensionAware.java)
>> 테스트 코드 작성에서 반복되는 보안 코드를 정의
> 
> - ## [TestSecurityRunnerAware](./src/test/java/run/freshr/common/extensions/TestSecurityRunnerAware.java)
>> Application Run 마지막에 동작하도록 구성한 추상 클래스  
>> [TestSecurityExtensionAware](./src/test/java/run/freshr/common/extensions/TestSecurityExtensionAware.java) 와 함께 사용하도록 기능 정의
> 
> - ## [TestSecurityServiceAware](./src/test/java/run/freshr/service/TestSecurityServiceAware.java)
>> 테스트 데이터를 관리하기 위한 service 공통 기능을 설계한 인터페이스  
>> [TestSecurityExtensionAware](./src/test/java/run/freshr/common/extensions/TestSecurityExtensionAware.java) 와 함께 사용하도록 기능 정의
> 
> - ## [PrintUtil](./src/test/java/run/freshr/common/utils/PrintUtil.java)
>> RestDocs 기능 작성을 돕는 편의 기능을 정의