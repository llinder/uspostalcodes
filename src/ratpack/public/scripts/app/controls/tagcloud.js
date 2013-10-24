/*global define,window*/
define(['can', 'app/models'],
  function (can, models) {
    'use strict';

    return can.Control({
      defaults: {
        view: 'views/tagcloud.ejs'
      }
    }, {
      init: function () {
        var self = this;
        this.observe = new can.Observe();

        this.element.html(can.view(this.options.view, this.observe, {
          // EJS view helper style function.
          style: function(item) {
            return function(el) {
              // set size of individual elements in the tag cloud
              var size = item.count * 2.5 / self.max;
              $(el).css('font-size', size+'em');
            }
          }
        }));

        this.loadStates();

      },
      loadStates: function() {
        var self = this;
        this.observe.removeAttr('counts');
        self.max = 0;
        $.when(models.getStates()).then(
          function(counts) {

            // get the max postal code count out of all states
            counts.forEach(function(item){
              self.max = Math.max(self.max, item.count);
            });
            // add state counts to the observe. this triggers
            // template bindings and causes DOM updates.
            self.observe.attr('counts', counts);
          }
        )
      },
      '.clear click': function(el) {
        var self = this;
        $.when(models.clearStates()).then(self.loadStates());
      }
    });

  });
