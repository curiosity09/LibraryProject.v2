import NamedEntityForm from '../../components/NamedEntityForm';
import { authorApi } from '../../api/endpoints';

export default function AddAuthorPage() {
  return (
    <NamedEntityForm
      labelKey="common.author"
      placeholderKey="placeholder.author"
      onSubmit={(value) => authorApi.create({ id: null, fullName: value })}
      redirectTo="/librarian"
    />
  );
}
