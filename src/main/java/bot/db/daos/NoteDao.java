package bot.db.daos;

import bot.db.objects.Note;

public class NoteDao extends BaseDaoImpl<Note>{
    @Override
    protected Class<Note> getGenericClass() {
        return Note.class;
    }
}
