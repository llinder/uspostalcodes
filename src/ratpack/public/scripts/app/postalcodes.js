/*global define,window*/
define(['can', 'app/controls/tagcloud', 'spin', 'jquery-spin'],
  function (can, TagCloud) {
    'use strict';

    return can.Control({
      defaults: {
        view: 'views/postalcodes.ejs'
      }
    }, {
      init: function () {
        // render
        this.element.html(can.view(this.options.view, this.options));

        this.tagcloud = new TagCloud('#tagcloud');
      }
    });

  });
