package macom.inote.repository

import android.util.Log
import macom.inote.data.Note
import macom.inote.intenet.RetrofitInstance
import macom.inote.room.NoteDao

/**
 * 笔记仓库
 */
class NoteRepository(private val noteDao: NoteDao) {
    /**
     * 本地Room数据库,同时发出服务端请求，网络超时返回false
     */

    // 插入本地数据库
    suspend fun insert(note: Note): Boolean {
        noteDao.insert(note)
        return addNoteToServer(note)  // 将本地新增的笔记同步到云端
    }

    // 更新本地数据库
    suspend fun update(note: Note): Boolean {
        noteDao.insert(note)
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
     * 同步单个笔记时不报异常_同步所有笔记时候出现网络问题会报异常
     */
    suspend fun syncAllNotes(user: String): Boolean {
        try {
            // 获取本地所有笔记并同步到云端
            noteDao.getAll(user).forEach {
                RetrofitInstance.noteApi.updateNote(it)
            }
            // 从云端获取所有笔记并同步到本地
            RetrofitInstance.noteApi.getAllNotes(user).forEach {
                noteDao.insert(it)
            }
            return true // 如果所有操作成功，则返回 true
        } catch (e: Exception) {
            Log.d("网络", "同步所有笔记失败:", e)
            return false // 捕获到异常时返回 false
        }
    }


    // 将本地新增的笔记同步到云端
    suspend fun addNoteToServer(note: Note): Boolean {
        try {
            RetrofitInstance.noteApi.createNote(note)
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
            RetrofitInstance.noteApi.updateNote(note)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步更新笔记失败", e)
            return false
        }
    }

    // 删除云端笔记
    private suspend fun deleteNoteFromServer(note: Note): Boolean {
        try {
            RetrofitInstance.noteApi.deleteNote(note)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步删除笔记失败", e)
            return false
        }
    }

}
