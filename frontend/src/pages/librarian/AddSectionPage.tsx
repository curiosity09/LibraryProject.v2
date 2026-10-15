import NamedEntityForm from '../../components/NamedEntityForm';
import { sectionApi } from '../../api/endpoints';

export default function AddSectionPage() {
  return (
    <NamedEntityForm
      labelKey="common.section"
      placeholderKey="placeholder.section"
      onSubmit={(value) => sectionApi.create({ id: null, name: value })}
      redirectTo="/librarian"
    />
  );
}
