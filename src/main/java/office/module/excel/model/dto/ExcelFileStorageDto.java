package office.module.excel.model.dto;

import com.google.common.hash.Hashing;
import lombok.*;

import javax.sql.rowset.serial.SerialBlob;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.sql.SQLException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class ExcelFileStorageDto {
    private Long id;
    @NotNull
    private Blob fileData;
    @NotNull
    private String fileName;
    private String url;
    private String contentType;
    private String creator;
    private String owner;

    public static class Builder {
        private byte[] fileData;
        private String fileName;
        private String url;
        private String contentType;
        private String creator;
        private String owner;

        public Builder fileData(byte[] fileData) {
            this.fileData = fileData;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder creator(String creator) {
            this.creator = creator;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public ExcelFileStorageDto build() throws SQLException {
            long id = Hashing.sha256().hashBytes(fileData).asLong();
            this.fileName = this.fileName != null? this.fileName : String.valueOf(id);
            String url = this.url != null ? this.url : this.owner + '/' + this.fileName;
            return new ExcelFileStorageDto(id, new SerialBlob(fileData), url, fileName, contentType, creator, owner);
        }
    }
}
