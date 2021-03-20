package com.kelaut.fisherman.util


enum class Status {
    // Error for input
    ErrEmptyField,
    ErrWrongFormat,
    ErrTooShort,
    ErrNotMatch,

    // Error for registration
    ErrCreatingUser,
    ErrSavingData,

    // Error for login
    ErrCredential,

    // Success
    Success,

    ErrZeroValue,

    ErrUploadImage
}

