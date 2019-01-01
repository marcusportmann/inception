/*
 * Copyright 2018 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {Component, OnInit} from '@angular/core';

@Component({
  templateUrl: 'colors.component.html'
})
export class ColorsComponent implements OnInit {

  minIEVersion = 10;

  constructor() {
  }

  getCssCustomProperties() {
    var cssCustomProperties = {};
    var sheets: any = document.styleSheets;
    var cssText = '';

    for (var i = sheets.length - 1; i > -1; i--) {
      var rules = sheets[i].cssRules;

      for (var j = rules.length - 1; j > -1; j--) {
        if (rules[j].selectorText === '.ie-custom-properties') {
          cssText = rules[j].cssText;
          break;
        }
      }

      if (cssText) {
        break;
      }
    }

    cssText = cssText.substring(cssText.lastIndexOf('{') + 1, cssText.lastIndexOf('}'));
    cssText.split(';').forEach(function (property) {
      if (property) {
        var name = property.split(': ')[0];
        var value = property.split(': ')[1];

        if (name && value) {
          cssCustomProperties["--" + name.trim()] = value.trim();
        }
      }
    });
    return cssCustomProperties;
  };

  getStyle(property, element): string {
    if (element === void 0) {
      element = document.body;
    }

    var style;

    if (this.isCustomProperty(property) && this.isIE1x()) {
      var cssCustomProperties = this.getCssCustomProperties();
      style = cssCustomProperties[property];
    } else {
      style = window.getComputedStyle(element, null).getPropertyValue(property).replace(/^\s/, '');
    }

    return style;
  };

  isCustomProperty(property): boolean {
    return property.match(/^--.*/i);
  };

  isIE1x(): boolean {
    var anyDocument: any = document;
    return Boolean(anyDocument.documentMode) && anyDocument.documentMode >= this.minIEVersion;
  };

  ngOnInit(): void {
    this.themeColors();
  }

  rgbToHex(color): string {
    if (typeof color === 'undefined') {
      throw new Error('Hex color is not defined');
    }

    var rgb = color.match(/^rgba?[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?/i);

    if (!rgb) {
      throw new Error(color + " is not a valid rgb color");
    }

    var r = "0" + parseInt(rgb[1], 10).toString(16);
    var g = "0" + parseInt(rgb[2], 10).toString(16);
    var b = "0" + parseInt(rgb[3], 10).toString(16);
    return "#" + r.slice(-2) + g.slice(-2) + b.slice(-2);
  };

  themeColors(): void {
    Array.from(document.querySelectorAll('.theme-color')).forEach((el) => {
      let elem = document.getElementsByClassName(el.classList[0])[0];

      let background = this.getStyle('background-color', elem);

      let table = document.createElement('table');
      table.innerHTML = `
        <table class="w-100">
          <tr>
            <td class="text-muted" style="font-size: 10px;">HEX:</td>
            <td class="font-weight-bold" style="font-size: 10px;">${this.rgbToHex(background)}</td>
          </tr>
          <tr>
            <td class="text-muted" style="font-size: 10px;">RGB:</td>
            <td class="font-weight-bold" style="font-size: 10px;">${background}</td>
          </tr>
        </table>
      `;

      el.parentNode.appendChild(table);
    });
  }
}

