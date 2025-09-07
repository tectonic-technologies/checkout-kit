import eslint from '@eslint/js';
import perfectionist from 'eslint-plugin-perfectionist';
import tseslint from 'typescript-eslint';

export default tseslint.config(eslint.configs.recommended, tseslint.configs.recommended, {
  ignores: ['src/demo/**/*'],
  plugins: {
    perfectionist,
  },
  rules: {
    '@typescript-eslint/no-explicit-any': 'off',
    '@typescript-eslint/explicit-module-boundary-types': 'off',
    '@typescript-eslint/no-inferrable-types': 'off',
    '@typescript-eslint/no-non-null-assertion': 'off',
    '@typescript-eslint/no-empty-interface': 'off',
    '@typescript-eslint/no-namespace': 'off',
    '@typescript-eslint/no-empty-function': 'off',
    '@typescript-eslint/no-this-alias': 'off',
    '@typescript-eslint/ban-types': 'off',
    '@typescript-eslint/ban-ts-comment': 'off',
    'prefer-spread': 'off',
    'no-case-declarations': 'off',
    'no-console': 'off',
    '@typescript-eslint/no-empty-object-type': 'off',
    '@typescript-eslint/no-unused-vars': [
      'error',
      {
        // ignores any variable whose name begins with _
        varsIgnorePattern: '^_',
        // ignores any function argument whose name begins with _
        argsIgnorePattern: '^_',
        ignoreRestSiblings: true,
      },
    ],
    '@typescript-eslint/no-unnecessary-condition': 'off',

    // ts-rules
    '@typescript-eslint/consistent-type-imports': 'error',

    'perfectionist/sort-imports': [
      'error',
      {
        type: 'alphabetical',
        order: 'asc',
        fallbackSort: { type: 'unsorted' },
        ignoreCase: true,
        specialCharacters: 'keep',
        internalPattern: ['^~/.+', '^@/.+'],
        partitionByComment: false,
        partitionByNewLine: false,
        newlinesBetween: 'always',
        maxLineLength: undefined,
        groups: [
          ['value-builtin', 'value-external', 'value-internal'],
          ['value-parent', 'value-sibling', 'value-index'],
          ['type-builtin', 'type-external', 'type-internal'],
          ['type-parent', 'type-sibling', 'type-index'],
          'ts-equals-import',
          'unknown',
        ],

        customGroups: [],
        environment: 'node',
      },
    ],
  },
});
