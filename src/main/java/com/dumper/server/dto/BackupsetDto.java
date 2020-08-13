package com.dumper.server.dto;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class BackupsetDto {

    private final int backupSetId;
    private final UUID backupSetUuid;
    private final int mediaSetId;
    private final int firstFamilyNumber;
    private final int firstMediaNumber;
    private final int lastFamilyNumber;
    private final int lastMediaNumber;
    private final int catalogFamilyNumber;
    private final int catalogMediaNumber;
    private final int position;
    private final LocalDateTime expirationDate;
    private final int softwareVendorId;
    private final String name;
    private final String description;
    private final String userName;
    private final int softwareMajorVersion;
    private final int softwareMinorVersion;
    private final int softwareBuildVersion;
    private final int timeZone;
    private final int mtfMinorVersion;
    private final int firstLsn;
    private final int lastLsn;
    private final int checkpointLsn;
    private final int databaseBackupLsn;
    private final LocalDateTime databaseCreationDate;
    private final LocalDateTime backupStartDate;
    private final LocalDateTime backupFinishDate;
    private final char type;
    private final int sortOrder;
    private final int codePage;
    private final int compatibilityLevel;
    private final int databaseVersion;
    private final int backupSize;
    private final String databaseName;
    private final String serverName;
    private final String machineName;
    private final int flags;
    private final int unicodeLocale;
    private final int unicodeCompareStyle;
    private final String collationName;
    private final boolean isPasswordProtected;
    private final String recoveryModel;
    private final boolean hasBulkLoggedData;
    private final boolean isSnapshot;
    private final boolean isReadonly;
    private final boolean isSingleUser;
    private final boolean hasBackupChecksums;
    private final boolean isDamaged;
    private final boolean beginsLogChain;
    private final boolean hasIncompleteMetadata;
    private final boolean isForceOffline;
    private final boolean isCopyOnly;
    private final UUID firstRecoveryForkGuid;
    private final UUID lastRecoveryForkGuid;
    private final int forkPointLsn;
    private final UUID databaseGuid;
    private final UUID familyGuid;
    private final int differentialBaseLsn;
    private final UUID differentialBaseGuid;
    private final int compressedBackupSize;
    private final String keyAlgorithm;
    private final String encryptorThumbprint;
    private final String encryptorType;
}
