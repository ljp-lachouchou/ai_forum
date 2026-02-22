package ai.ljp.database

import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

object DatabaseMigrations {
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SQLiteConnection) {
            db.execSQL("""
            CREATE TABLE IF NOT EXISTS `bookmarks_new` (
                `id` TEXT NOT NULL, 
                `userId` TEXT NOT NULL, 
                `postId` TEXT NOT NULL, 
                `createdAt` INTEGER NOT NULL, 
                `updatedAt` INTEGER NOT NULL, 
                `syncState` INTEGER NOT NULL, 
                `deleted` INTEGER NOT NULL, 
                PRIMARY KEY(`userId`, `postId`)
            )
        """.trimIndent())

            db.execSQL("""
            INSERT OR REPLACE INTO `bookmarks_new` (id, userId, postId, createdAt, updatedAt, syncState, deleted)
            SELECT id, userId, postId, createdAt, 
                   IFNULL(updatedAt, createdAt), -- 补全缺失列
                   IFNULL(syncState, 0),          -- 默认 Pending (假设 0)
                   deleted FROM `bookmarks`
        """.trimIndent())

            db.execSQL("DROP TABLE `bookmarks`")
            db.execSQL("ALTER TABLE `bookmarks_new` RENAME TO `bookmarks`")

            db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmarks_postId` ON `bookmarks` (`postId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmarks_userId` ON `bookmarks` (`userId`)")


            db.execSQL("""
            CREATE TABLE IF NOT EXISTS `likes_new` (
                `id` TEXT NOT NULL, 
                `userId` TEXT NOT NULL, 
                `postId` TEXT NOT NULL, 
                `createdAt` INTEGER NOT NULL, 
                `updatedAt` INTEGER NOT NULL, 
                `syncState` INTEGER NOT NULL, 
                `deleted` INTEGER NOT NULL, 
                PRIMARY KEY(`userId`, `postId`)
            )
        """.trimIndent())

            db.execSQL("""
            INSERT OR REPLACE INTO `likes_new` (id, userId, postId, createdAt, updatedAt, syncState, deleted)
            SELECT id, userId, postId, createdAt, 
                   IFNULL(updatedAt, createdAt), 
                   IFNULL(syncState, 0), 
                   deleted FROM `likes`
        """.trimIndent())

            db.execSQL("DROP TABLE `likes`")
            db.execSQL("ALTER TABLE `likes_new` RENAME TO `likes`")

            db.execSQL("CREATE INDEX IF NOT EXISTS `index_likes_postId` ON `likes` (`postId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_likes_userId` ON `likes` (`userId`)")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SQLiteConnection) {
            db.execSQL("DROP VIEW IF EXISTS `active_likes`")
            db.execSQL(
                "CREATE VIEW `active_likes` AS SELECT `userId`, `postId` FROM likes WHERE `deleted` = 0"
            )
            db.execSQL("DROP VIEW IF EXISTS `active_bookmarks`")
            db.execSQL(
                "CREATE VIEW `active_bookmarks` AS SELECT `userId`, `postId` FROM bookmarks WHERE `deleted` = 0"
            )
        }
    }

}
