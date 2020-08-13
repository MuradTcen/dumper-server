package com.dumper.server.entity;

import com.sun.source.doctree.SerialDataTree;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Entity
@Table(name = "backupset")
@Data
public class Backupset implements Serializable {

    @Id
    @Column
    private int backupSetId;
    @Column
    private UUID backupSetUuid;
    @Column
    private int mediaSetId;
    @Column
    private int firstFamilyNumber;
    @Column
    private int firstMediaNumber;
    @Column
    private int lastFamilyNumber;
    @Column
    private int lastMediaNumber;
    @Column
    private int catalogFamilyNumber;
    @Column
    private int catalogMediaNumber;
    @Column
    private int position;
    @Column
    private LocalDateTime expirationDate;
    @Column
    private int softwareVendorId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String userName;
    @Column
    private int softwareMajorVersion;
    @Column
    private int softwareMinorVersion;
    @Column
    private int softwareBuildVersion;
    @Column
    private int timeZone;
    @Column
    private int mtfMinorVersion;
    @Column
    private int firstLsn;
    @Column
    private int lastLsn;
    @Column
    private int checkpointLsn;
    @Column
    private int databaseBackupLsn;
    @Column
    private LocalDateTime databaseCreationDate;
    @Column
    private LocalDateTime backupStartDate;
    @Column
    private LocalDateTime backupFinishDate;
    @Column
    private char type;
    @Column
    private int sortOrder;
    @Column
    private int codePage;
    @Column
    private int compatibilityLevel;
    @Column
    private int databaseVersion;
    @Column
    private int backupSize;
    @Column
    private String databaseName;
    @Column
    private String serverName;
    @Column
    private String machineName;
    @Column
    private int flags;
    @Column
    private int unicodeLocale;
    @Column
    private int unicodeCompareStyle;
    @Column
    private String collationName;
    @Column
    private boolean isPasswordProtected;
    @Column
    private String recoveryModel;
    @Column
    private boolean hasBulkLoggedData;
    @Column
    private boolean isSnapshot;
    @Column
    private boolean isReadonly;
    @Column
    private boolean isSingleUser;
    @Column
    private boolean hasBackupChecksums;
    @Column
    private boolean isDamaged;
    @Column
    private boolean beginsLogChain;
    @Column
    private boolean hasIncompleteMetadata;
    @Column
    private boolean isForceOffline;
    @Column
    private boolean isCopyOnly;
    @Column
    private UUID firstRecoveryForkGuid;
    @Column
    private UUID lastRecoveryForkGuid;
    @Column
    private Integer forkPointLsn;
    @Column
    private UUID databaseGuid;
    @Column
    private UUID familyGuid;
    @Column
    private Integer differentialBaseLsn;
    @Column
    private UUID differentialBaseGuid;
    @Column
    private Integer compressedBackupSize;
    @Column
    private String keyAlgorithm;
    @Column
    private String encryptorThumbprint;
    @Column
    private String encryptorType;
}
