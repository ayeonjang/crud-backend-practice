kr.ac.jbnu.ayj.wsd
├── api
│   ├── dto
│   │   └── ItemDto.java
│   ├── request
│   │   ├── ItemCreateRequest.java
│   │   └── ItemUpdateRequest.java
│   └── v1
│       └── ItemController.java
└── config
    ├── LoggingInterceptor.java
    └── WebConfig.java

[ 미들 웨어 ( Middleware ) 구현 ]
> Logging Interceptor.java :
  - preHandle(): Controller 진입 전 요청 HTTP Method, URI를 콘솔 로그로 출력합니다.
  - afterCompletion(): Controller 응답 완료 후 처리 결과 HTTP Status 코드를 로그로 출력합니다.

> WebConfig.java :
  - WebMvcConfigurer를 구현하여 작성한 LoggingInterceptor를 등록합니다.
  - /api/** 경로로 들어오는 모든 요청에 대해 로그 미들웨어가 동작하도록 설정합니다.

[ API 명세 및 HTTP 상태 코드]
> GET > /api/v1/items > 전체 아이템 목록 조회 > 200 OK
> GET > /api/v1/items/{id} > 아이템 단건 조회 > 200 OK / 404 Not Found
> POST > /api/v1/items > 신규 아이템 생성 > 201 Created
> POST > /api/v1/items/validate > 헤더(X-USER-ID) 검증 테스트 > 200 OK / 400 Bad Request
> PUT > /api/v1/items/{id} > 아이템 정보 수정 > 200 OK / 404 Not Found
> PUT > /api/v1/items/error/{id} > 서버 내부 에러 시뮬레이션 > 500 Internal Server Error
> DELETE > /api/v1/items/{id} > 아이템 삭제 > 204 No Content / 404 Not Found
> DELETE > /api/v1/items/system/{id} > 시스템 점검 에러 시뮬레이션 > 503 Service Unavailable
