/*global define*/
define(['jquery', 'can'],
  function ($, can) {
    'use strict';

    var m = {};

    // can JS model object used to represent results
    // from the postal code count service
    m.State = can.Model.extend({
      findAll: 'GET /api/postalcode/count'
    }, {});

    // retrieves a collection of State objects. if the
    // objects have already been retrieved then a new
    // deferred is returned for API consistency.
    m.getStates = function() {
      if(m.__states === undefined || m.__states === null) {
        return m.State.findAll({}, function(states){
          m.__states = states;
        })
      } else {
        var def = new can.Deferred();
        def.resolve(m.__states);
      }
    };

    m.clearStates = function() {
      m.__states = null;
      $.get('api/postalcode/clear');
    }

    return m;

  });
