/*global require*/
require.config({
  baseUrl: 'scripts',
  locale: 'en',
  paths: {
    'jquery': 'lib/jquery/1.10.2/jquery',
    'can': 'lib/can/2.0.0/can',
    'spin': 'lib/spin/1.3.2/spin',
    'jquery-spin': 'lib/jquery-spin/1.0/spin',
    'app': 'app'
  },
  shim: {
    jquery: {
      exports: '$'
    },
    spin: {
      deps: ['jquery'],
      exports: 'Spinner'
    },
    'jquery-spin': {
      deps: ['spin', 'jquery'],
      exports: 'jQuery.fn.spin'
    }
  }
});

require([
  'jquery', 'can/route', 'can/view/ejs', 'app/postalcodes', 'spin', 'jquery-spin'],
  function ($, route, EJS, PostalCodes) {
    'use strict';

    // -- add present for spinner
    $.fn.spin.presets.default = {
      lines: 8, length: 4, width: 3, radius: 5, color: '#000000', top: 10
    };

    // -- ajax configuration
    $.ajaxSetup({
      contentType: "application/json; charset=utf-8"
    });
    $.ajaxPrefilter(function(options, orig, xhr ) {
      if ( options.processData
        && /^application\/json((\+|;).+)?$/i.test( options.contentType )
        && /^(post|put|delete)$/i.test( options.type )
        ) {
        options.data = JSON.stringify( orig.data );
      }
    });
    $(document).bind({
      'ajaxError': function(event, jqXHR, ajaxSettings, thrownError) {
        alert('An unexpected error occurred: '+thrownError);
      }
    });

    // Delay routing until we initialized everything
    route.ready(false);

    new PostalCodes("#app");

    route.ready(true);
  });
