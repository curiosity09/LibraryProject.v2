import NamedEntityForm from '../../components/NamedEntityForm';
import { genreApi } from '../../api/endpoints';

export default function AddGenrePage() {
  return (
    <NamedEntityForm
      labelKey="common.genre"
      placeholderKey="placeholder.genre"
      onSubmit={(value) => genreApi.create({ id: null, name: value })}
      redirectTo="/librarian"
    />
  );
}
