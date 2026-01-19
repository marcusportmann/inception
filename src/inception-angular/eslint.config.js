// @ts-check
const eslint = require("@eslint/js");
const tseslint = require("typescript-eslint");
const angular = require("angular-eslint");

module.exports = tseslint.config(
  {
    files: ["**/*.ts"],
    extends: [
      eslint.configs.recommended,
      ...tseslint.configs.recommended,
      ...tseslint.configs.stylistic,
      ...angular.configs.tsRecommended,
    ],
    processor: angular.processInlineTemplates,
    rules: {
      // ---- Existing selector rules (kept) ----
      "@angular-eslint/directive-selector": [
        "error",
        {
          type: "attribute",
          prefix: "app",
          style: "camelCase",
        },
      ],
      "@angular-eslint/component-selector": [
        "error",
        {
          type: "element",
          prefix: ["app", "inception"],
          style: "kebab-case",
        },
      ],

      // ---- Angular best-practice rules (added) ----
      "@angular-eslint/component-class-suffix": ["error", { suffixes: ["Component"] }],
      "@angular-eslint/directive-class-suffix": ["error", { suffixes: ["Directive"] }],

      // Enforce good/consistent Angular metadata patterns
      "@angular-eslint/use-injectable-provided-in": "error",
      "@angular-eslint/use-lifecycle-interface": "error",
      "@angular-eslint/use-pipe-transform-interface": "error",

      // Performance / maintainability defaults
      "@angular-eslint/prefer-on-push-component-change-detection": "error",
      "@angular-eslint/use-component-view-encapsulation": "error",

      // Common footguns
      "@angular-eslint/no-conflicting-lifecycle": "error",
      "@angular-eslint/no-empty-lifecycle-method": "warn",
      "@angular-eslint/no-input-rename": "error",
      "@angular-eslint/no-output-rename": "error",
      "@angular-eslint/no-forward-ref": "error",

      // ---- Static property naming (added) ----
      // Forces e.g. `static readonly API_URL = '...'`
      // and rejects `static apiUrl = '...'`
      "@typescript-eslint/naming-convention": [
        "error",
        {
          selector: "classProperty",
          modifiers: ["readonly", "static"],
          format: ["UPPER_CASE"],
        },
      ],
    },
  },
  {
    files: ["**/*.html"],
    extends: [
      ...angular.configs.templateRecommended,

      // Adds the Angular ESLint template accessibility preset
      // (alt-text, valid-aria, click-events-have-key-events, etc.)
      ...angular.configs.templateAccessibility,
    ],
    rules: {
      // A couple of high-signal template rules to enforce explicitly:
      "@angular-eslint/template/use-track-by-function": "warn",
      "@angular-eslint/template/button-has-type": "error",
    },
  }
);
