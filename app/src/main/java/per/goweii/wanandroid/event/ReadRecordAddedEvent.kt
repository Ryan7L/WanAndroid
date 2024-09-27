package per.goweii.wanandroid.event

import per.goweii.wanandroid.db.model.ReadRecordModel

class ReadRecordAddedEvent (val readRecordModel: ReadRecordModel): BaseEvent() {
}