package com.innople.devpleyground.dpfpapi.common.constants;

import lombok.Getter;

public class ErrorConstants {
    @Getter
    public enum ErrorCode {
        Invalid_KeyOrID("COM_001", "해당 KEY 또는 ID를 찾을 수 없습니다.", 1001),
        Invalid_Parameter("COM_002", "파라미터 정보가 정상적이지 않습니다.", 1002),
        Invalid_FileType("COM_003", "비정상적인 파일타입 입니다.", 1003),
        NOT_EXISTS_DATA("COM_004", "요청하신 데이터가 존재하지 않습니다.", 1004),
        NotFoundFolder_ByID("COM_005", "폴더ID에 맞는 폴더 정보가 존재하지 않습니다.", 1005),
        NotFoundContract_ByID("COM_006", "ID에 맞는 문서(계약) 정보가 존재하지 않습니다.", 1006),
        NotFoundTemplate_ByID("COM_007", "ID에 맞는 템플릿 정보가 존재하지 않습니다.", 1007),
        Invalid_ParameterType("COM_008", "파라미터 형식이 일치하지 않습니다.", 1008),
        Include_Forbidden_Word("COM_009", "금지어가 포함되어 있습니다.", 1009);

        private final String code;
        private final String message;
        private final int status;

        ErrorCode(String code, String message, int status) {
            this.code = code;
            this.message = message;
            this.status = status;
        }
    }
}
