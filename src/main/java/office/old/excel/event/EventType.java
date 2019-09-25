package office.old.excel.event;

// TODO future feature
public enum EventType {
    EXTRACT_SHEET, // phân tách các sheet trong 1 file excel
    PARSE_TEMPLATE, // phân tích một file excel để trích xuất template
    UPDATE_DATA, // update data cho một template, trả về file excel
    BUILD_VIEW, // chuyển excel thành html
    EXTRACT_MODEL, // trích xuất dữ liệu từ file excel
    PAGING, // phân trang cho một file excel
    REFORMAT, // format lại định dạng file excel
}
