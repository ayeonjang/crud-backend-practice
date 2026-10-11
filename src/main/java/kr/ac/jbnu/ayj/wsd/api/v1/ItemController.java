package kr.ac.jbnu.ayj.wsd.api.v1;

import kr.ac.jbnu.ayj.wsd.api.dto.ItemDto;
import kr.ac.jbnu.ayj.wsd.api.request.ItemCreateRequest;
import kr.ac.jbnu.ayj.wsd.api.request.ItemUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final Map<Long, ItemDto> store = new HashMap<>();
    private long sequence = 1L;
    private Map<String, Object> createErrorResponse(int statusCode, String message) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("statusCode", statusCode);
        errorMap.put("message", message);
        return errorMap;
    }

    // [GET 1] 전체 목록 조회 -> 200 OK (배열 반환)
    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems() {
        List<ItemDto> list = new ArrayList<>(store.values());
        return ResponseEntity.ok(list);
    }

    // [GET 2] 단건 상세 조회 -> 200 OK (객체) 또는 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable Long id) {
        ItemDto item = store.get(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(404, "해당 아이템을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(item);
    }

    // [POST 1] 자원 생성 -> 201 Created (생성된 객체 반환)
    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemCreateRequest request) {
        ItemDto item = new ItemDto();
        item.setId(sequence++);
        item.setName(request.getName());
        item.setPrice(request.getPrice());

        store.put(item.getId(), item);

        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    // [POST 2] 헤더 검증 시뮬레이션 -> 200 OK 또는 400 Bad Request
    @PostMapping("/validate")
    public ResponseEntity<Object> validateHeader(
            @RequestHeader(value = "X-USER-ID", required = false) String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(400, "X-USER-ID 헤더가 누락되었습니다."));
        }
        Map<String, String> successMap = new HashMap<>();
        successMap.put("message", "인증 완료: " + userId);
        return ResponseEntity.ok(successMap);
    }

    // [PUT 1] 자원 수정 -> 200 OK (객체) 또는 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request
    ) {
        ItemDto item = store.get(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(404, "수정할 아이템이 존재하지 않습니다."));
        }

        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }

        return ResponseEntity.ok(item);
    }

    // [PUT 2] 강제 서버 오류 시뮬레이션 -> 500 Internal Server Error
    @PutMapping("/error/{id}")
    public ResponseEntity<Object> updateItemWithError(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(500, "서버 처리 중 오류가 발생했습니다."));
    }

    // [DELETE 1] 자원 삭제 -> 204 No Content (본문 없음) 또는 404 Not Found
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long id) {
        ItemDto removed = store.remove(id);
        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(404, "삭제할 아이템이 존재하지 않습니다."));
        }
        return ResponseEntity.noContent().build();
    }

    // [DELETE 2] 점검 중 삭제 실패 시뮬레이션 -> 503 Service Unavailable
    @DeleteMapping("/system/{id}")
    public ResponseEntity<Object> deleteSystemItem(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(createErrorResponse(503, "현재 시스템 점검 중입니다."));
    }
}