DrumkitEventTypes {
	*new {
		Event.addEventType(\drums, { |server|
			// sample library / packs
			~kicksnd = ~kicksnd ? [];
			~snsnd = ~snsnd ? [];
			// sample index
			~kickn = ~kickn ? 0;
			~snn = ~snn ? 0;
			// rhythms
			~k = ~k ? Pseq([1,\r], inf);
			~sn = ~sn ? Pseq([\r,1], inf);
			//
			~channels = ~channels ? 2;
			~instrument = [\playbufm, \playbuf][~channels-1];
			~buf = [
				~kicksnd.at(~kickn.mod(~kicksnd.size)) * ~k,
				~snsnd.at(~snn.mod(~snsnd.size)) * ~sn,
			];
			~type = \note;
			currentEnvironment.play;

		},
			// defaults
			(legato: 1))
	}
}