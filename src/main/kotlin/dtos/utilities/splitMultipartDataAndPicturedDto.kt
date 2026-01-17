package com.example.dtos.utilities

data class splitMultipartDataAndPicturedDto(val jsonData: String?,
                                            val fileBytes: ByteArray?,
                                            val originalFileName: String?,
                                            var type: String?) {

    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (jsonData == null) errors.add("No json data found")
        if (fileBytes == null) errors.add("No file data found")
        if (originalFileName == null) errors.add("No file name found")

        return errors
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as splitMultipartDataAndPicturedDto

        if (jsonData != other.jsonData) return false
        if (!fileBytes.contentEquals(other.fileBytes)) return false
        if (originalFileName != other.originalFileName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = jsonData.hashCode()
        result = 31 * result + fileBytes.contentHashCode()
        result = 31 * result + (originalFileName?.hashCode() ?: 0)
        return result
    }
}