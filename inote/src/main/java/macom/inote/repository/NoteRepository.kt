package macom.inote.repository

import android.util.Log
import macom.inote.data.Note
import macom.inote.intenet.RetrofitInstance
import macom.inote.room.NoteDao
import retrofit2.await

/**
 * 笔记仓库
 */
class NoteRepository(private val noteDao: NoteDao) {
    /**
     * 本地Room数据库
     */

    // 插入本地数据库
    suspend fun insert(note: Note): Boolean {
        noteDao.insert(note)
        return addNoteToServer(note)  // 将本地新增的笔记同步到云端
    }

    // 更新本地数据库
    suspend fun update(note: Note): Boolean {
        noteDao.update(note)
        return updateNoteToServer(note)  // 将本地更新的笔记同步到云端
    }

    // 删除本地数据库
    suspend fun delete(note: Note) {
        noteDao.delete(note)
        deleteNoteFromServer(note)  // 删除云端笔记
    }

    // 获取所有本地笔记
    suspend fun getAll(user: String): List<Note> {
        val all = noteDao.getAll(user)
        return all
    }


    /**
     * 同步服务器操作
     */

    // 获取所有笔记（从云端同步到本地）
    suspend fun getAllNotesFromServer(user: String): List<Note> {
        try {
            // 获取云端所有笔记
            val serverNotes = RetrofitInstance.noteApi.getAllNotes(user).await()
            return serverNotes
        } catch (e: Exception) {
            // 处理获取数据失败的情况，可能是网络问题等
            Log.d("网络", "同步所有笔记失败")
        }
        return listOf()
    }

    // 将本地新增的笔记同步到云端
    suspend fun addNoteToServer(note: Note): Boolean {
        try {
            // 如果是新增笔记，调用 createNote API
            RetrofitInstance.noteApi.createNote(note).await()
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步新增笔记失败", e)
            return false
        }
    }

    // 将本地新增的笔记同步到云端
    suspend fun updateNoteToServer(note: Note): Boolean {
        try {
            // 如果是新增笔记，调用 createNote API
            RetrofitInstance.noteApi.updateNote(note).await()
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步更新笔记失败")
            return false
        }
    }

    // 删除云端笔记
    private suspend fun deleteNoteFromServer(note: Note): Boolean {
        try {
            RetrofitInstance.noteApi.deleteNote(note).await()
            Log.d("网络", "同步删除笔记成功")
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步删除笔记失败")
            return false
        }
    }

}
