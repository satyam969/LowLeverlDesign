"""
Hotel Management System — UML Class Diagram Generator
Generates a proper UML class diagram using matplotlib and saves as PNG.
"""

import matplotlib.pyplot as plt
import matplotlib.patches as patches
from matplotlib.patches import FancyArrowPatch
import matplotlib.patheffects as pe
import numpy as np

# ─── Theme ────────────────────────────────────────────────────────────────────
BG          = '#0d1117'
CLASS_HDR   = '#1f4e79'
IFACE_HDR   = '#3b1f79'
ENUM_HDR    = '#1a4731'
BOX_BG      = '#161b22'
BOX_BORDER  = '#58a6ff'
IFACE_BORDER= '#a371f7'
ENUM_BORDER = '#3fb950'
TEXT_WHITE  = '#e6edf3'
TEXT_STEREO = '#a5d6ff'
TEXT_FIELD  = '#c9d1d9'
TEXT_METHOD = '#79c0ff'
TEXT_ENUM   = '#3fb950'
DIV_LINE    = '#30363d'
ARROW_COLOR = '#58a6ff'
IMPL_COLOR  = '#a371f7'
COMP_COLOR  = '#ff7b72'
AGG_COLOR   = '#ffa657'

LH      = 0.30   # line height
PAD     = 0.12   # padding inside section
HDR_H   = 0.52   # header base height
STEREO_H= 0.22   # extra height for stereotype line
FONT_SM = 7.5
FONT_NM = 8.5
FONT_LG = 9.5

# ─── Helper: draw one UML class box ───────────────────────────────────────────
def draw_class(ax, x, y, w,
               name, stereotype=None,
               fields=None, methods=None, values=None,
               hdr_color=CLASS_HDR, border_color=BOX_BORDER):
    """
    Draws a UML class box.
    Returns: (center_x, center_y, bottom_y)
    x,y = top-left corner of the box.
    """
    fields  = fields  or []
    methods = methods or []
    values  = values  or []

    hdr_h = HDR_H + (STEREO_H if stereotype else 0)
    fields_h  = (len(fields)  * LH + 2*PAD) if fields  else 0
    methods_h = (len(methods) * LH + 2*PAD) if methods else 0
    values_h  = (len(values)  * LH + 2*PAD) if values  else 0
    total_h   = hdr_h + fields_h + methods_h + values_h

    # ── Outer box ──
    rect = patches.FancyBboxPatch(
        (x, y - total_h), w, total_h,
        boxstyle="round,pad=0.03",
        facecolor=BOX_BG, edgecolor=border_color,
        linewidth=1.8, zorder=2
    )
    ax.add_patch(rect)

    # ── Header ──
    hdr = patches.FancyBboxPatch(
        (x, y - hdr_h), w, hdr_h,
        boxstyle="round,pad=0.03",
        facecolor=hdr_color, edgecolor=border_color,
        linewidth=0, zorder=3
    )
    ax.add_patch(hdr)

    # ── Stereotype & Name ──
    ty = y
    if stereotype:
        ty -= 0.20
        ax.text(x + w/2, ty, f'«{stereotype}»',
                ha='center', va='top', fontsize=FONT_SM,
                color=TEXT_STEREO, fontstyle='italic', zorder=5)
        ty -= STEREO_H
    else:
        ty -= 0.10

    ax.text(x + w/2, ty - 0.06, name,
            ha='center', va='top', fontsize=FONT_LG,
            fontweight='bold', color=TEXT_WHITE, zorder=5)

    cur_y = y - hdr_h

    # ── Fields section ──
    if fields:
        ax.plot([x, x+w], [cur_y, cur_y], color=DIV_LINE, linewidth=1.2, zorder=4)
        cur_y -= PAD
        for f in fields:
            cur_y -= LH
            ax.text(x + 0.13, cur_y + LH*0.35, f,
                    ha='left', va='center', fontsize=FONT_SM,
                    color=TEXT_FIELD, fontfamily='monospace', zorder=5)
        cur_y -= PAD

    # ── Methods section ──
    if methods:
        ax.plot([x, x+w], [cur_y, cur_y], color=DIV_LINE, linewidth=1.2, zorder=4)
        cur_y -= PAD
        for m in methods:
            cur_y -= LH
            ax.text(x + 0.13, cur_y + LH*0.35, m,
                    ha='left', va='center', fontsize=FONT_SM,
                    color=TEXT_METHOD, fontfamily='monospace', zorder=5)
        cur_y -= PAD

    # ── Enum values section ──
    if values:
        ax.plot([x, x+w], [cur_y, cur_y], color=DIV_LINE, linewidth=1.2, zorder=4)
        cur_y -= PAD
        for v in values:
            cur_y -= LH
            ax.text(x + 0.13, cur_y + LH*0.35, v,
                    ha='left', va='center', fontsize=FONT_SM,
                    color=TEXT_ENUM, fontfamily='monospace', zorder=5)
        cur_y -= PAD

    bot_y  = y - total_h
    cx     = x + w / 2
    cy     = y - total_h / 2
    return {'cx': cx, 'cy': cy,
            'top': y, 'bot': bot_y,
            'left': x, 'right': x+w,
            'mid_left':  (x,     cy),
            'mid_right': (x+w,   cy),
            'mid_top':   (cx,    y),
            'mid_bot':   (cx,    bot_y)}


# ─── Helper: arrows ───────────────────────────────────────────────────────────
def arrow(ax, x1, y1, x2, y2,
          style='->',
          color=ARROW_COLOR,
          lw=1.4,
          dashed=False,
          label=None,
          label_offset=(0, 0.15)):
    ls = '--' if dashed else '-'
    # Draw line
    ax.annotate('',
        xy=(x2, y2), xytext=(x1, y1),
        arrowprops=dict(
            arrowstyle=style,
            color=color,
            lw=lw,
            linestyle=ls,
            connectionstyle='arc3,rad=0.0'
        ), zorder=1)
    if label:
        mx = (x1+x2)/2 + label_offset[0]
        my = (y1+y2)/2 + label_offset[1]
        ax.text(mx, my, label, fontsize=6.5, color=color,
                ha='center', va='center',
                bbox=dict(boxstyle='round,pad=0.1', fc=BG, ec='none'),
                zorder=6)

def impl_arrow(ax, x1, y1, x2, y2):
    """Dashed arrow with hollow triangle — implements/realizes."""
    ax.annotate('',
        xy=(x2, y2), xytext=(x1, y1),
        arrowprops=dict(
            arrowstyle='-|>',
            color=IMPL_COLOR,
            lw=1.3,
            linestyle='--',
            mutation_scale=14
        ), zorder=1)

def comp_arrow(ax, x1, y1, x2, y2, label=None):
    """Solid line with filled diamond (composition)."""
    ax.plot([x1, x2], [y1, y2], color=COMP_COLOR, lw=1.4, zorder=1)
    # Draw diamond at source
    dx, dy = x2-x1, y2-y1
    length = np.hypot(dx, dy)
    if length == 0: return
    ux, uy = dx/length, dy/length
    px, py = -uy, ux
    s = 0.12
    diamond = plt.Polygon([
        [x1, y1],
        [x1 + s*ux + s*px/2, y1 + s*uy + s*py/2],
        [x1 + s*ux*2, y1 + s*uy*2],
        [x1 + s*ux + s*(-px)/2, y1 + s*uy + s*(-py)/2],
    ], closed=True, facecolor=COMP_COLOR, edgecolor=COMP_COLOR, zorder=2)
    ax.add_patch(diamond)
    arrow(ax, x1 + s*ux*2, y1 + s*uy*2, x2, y2,
          style='->', color=COMP_COLOR, lw=1.4, label=label)

def agg_arrow(ax, x1, y1, x2, y2, label=None):
    """Dashed line with hollow diamond (aggregation/dependency)."""
    ax.plot([x1, x2], [y1, y2], color=AGG_COLOR, lw=1.3, ls='--', zorder=1)
    if label:
        mx, my = (x1+x2)/2, (y1+y2)/2
        ax.text(mx, my+0.15, label, fontsize=6.5, color=AGG_COLOR,
                ha='center',
                bbox=dict(boxstyle='round,pad=0.1', fc=BG, ec='none'), zorder=6)


# ─── Layout constants ──────────────────────────────────────────────────────────
# Column x-positions
C0, C1, C2, C3, C4 = 0.3, 5.2, 10.8, 17.5, 23.0
# Box widths
W_WIDE  = 4.5
W_MED   = 4.0
W_SLIM  = 3.5
W_XSLIM = 3.0

fig, ax = plt.subplots(figsize=(30, 22))
fig.patch.set_facecolor(BG)
ax.set_facecolor(BG)
ax.set_xlim(-0.2, 30)
ax.set_ylim(-0.5, 22)
ax.set_aspect('equal')
ax.axis('off')

# ── Title ──
ax.text(14.8, 21.6, 'Hotel Management System — UML Class Diagram',
        ha='center', va='center', fontsize=16, fontweight='bold',
        color=TEXT_WHITE,
        path_effects=[pe.withStroke(linewidth=4, foreground='#1f6feb33')])
ax.plot([0.3, 29.3], [21.2, 21.2], color='#30363d', lw=1)

# ═══════════════════════════════════════════════════════════════════
# CLASS DEFINITIONS
# ═══════════════════════════════════════════════════════════════════

# ── Column 0: Guest, Reservation, Enums ──────────────────────────
b_guest = draw_class(ax, C0, 20.8, W_MED, 'Guest',
    fields=[
        '- id : String',
        '- name : String',
        '- mail : String',
        '- phone : String',
        '- reservations : List<Reservation>',
    ],
    methods=[
        '+ addReservation(r : Reservation)',
        '+ getReservations() : List<Reservation>',
    ])

b_reservation = draw_class(ax, C0, 13.8, W_MED, 'Reservation',
    fields=[
        '- reservationId : String',
        '- guest : Guest',
        '- room : Room',
        '- checkIn : LocalDateTime',
        '- checkOut : LocalDateTime',
        '- status : ReserveStatus',
        '- totalPrice : double',
    ],
    methods=[
        '+ confirm()',
        '+ complete()',
        '+ cancel()',
        '+ getReservationId() : String',
        '+ getCheckIn() : LocalDateTime',
        '+ getCheckOut() : LocalDateTime',
        '+ getStatus() : ReserveStatus',
        '+ getRoom() : Room',
        '+ getGuest() : Guest',
    ])

b_resStatus = draw_class(ax, C0, 4.8, W_MED, 'ReserveStatus',
    stereotype='enumeration',
    values=['PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED'],
    hdr_color=ENUM_HDR, border_color=ENUM_BORDER)

# ── Column 1: HotelSystem, ReservationProcessor, RoomType ────────
b_hotel = draw_class(ax, C1, 20.8, W_WIDE, 'HotelSystem',
    stereotype='Singleton',
    fields=[
        '- instance : HotelSystem',
        '- reservationProcessor : ReservationProcessor',
        '- rooms : List<Room>',
        '- guests : List<Guest>',
        '- lck : ReentrantLock',
    ],
    methods=[
        '+ getInstance() : HotelSystem',
        '+ makeReservation(g, r, from, to)',
        '+ cancelReservation(id : String)',
        '+ checkIn(id : String)',
        '+ checkOut(id : String)',
        '+ addRoom(r : Room)',
        '+ addGuest(g : Guest)',
        '+ searchAvailableRooms(from, to) : List<Room>',
        '+ getRooms() : List<Room>',
        '+ getGuests() : List<Guest>',
    ])

b_processor = draw_class(ax, C1, 10.5, W_WIDE, 'ReservationProcessor',
    fields=[
        '- reservations : Map<String, Reservation>',
    ],
    methods=[
        '+ makeReservation(g, r, from, to)',
        '+ cancelReservation(id : String)',
        '+ checkIn(id : String)',
        '+ checkOut(id : String)',
    ])

b_roomType = draw_class(ax, C1, 4.8, W_MED, 'RoomType',
    stereotype='enumeration',
    values=['SINGLE', 'DOUBLE', 'SUITE'],
    hdr_color=ENUM_HDR, border_color=ENUM_BORDER)

# ── Column 2: Room, Calender, PricingStrategy, Strategies ────────
b_room = draw_class(ax, C2, 20.8, W_WIDE, 'Room',
    stereotype='Context',
    fields=[
        '- number : int',
        '- type : RoomType',
        '- state : RoomState',
        '- basePrice : double',
        '- calendar : Calender',
        '- pricing : PricingStrategy',
    ],
    methods=[
        '+ reserve(r : Reservation)',
        '+ occupy()',
        '+ release()',
        '+ maintenance()',
        '+ isAvailable(checkIn, checkOut) : boolean',
        '+ finalPrice() : double',
        '+ setState(state : RoomState)',
        '+ getNumber() : int',
        '+ getCalendar() : Calender',
    ])

b_calender = draw_class(ax, C2, 11.5, W_WIDE, 'Calender',
    fields=[
        '- reservations : List<Reservation>',
    ],
    methods=[
        '+ isAvailable(checkIn, checkOut) : boolean',
        '+ addReservation(r : Reservation)',
        '+ removeReservation(r : Reservation)',
    ])

b_pricing = draw_class(ax, C2, 6.8, W_WIDE, 'PricingStrategy',
    stereotype='interface',
    methods=[
        '+ calculatePrice(actual : double) : double',
    ],
    hdr_color=IFACE_HDR, border_color=IFACE_BORDER)

b_onSeason = draw_class(ax, C2, 4.0, W_MED, 'OnSeasonStrategy',
    fields=['- MULTIPLY_FACTOR : double = 1.5'],
    methods=['+ calculatePrice(actual : double) : double'])

b_offSeason = draw_class(ax, C2 + W_MED + 0.4, 4.0, W_MED, 'OffSeasonStrategy',
    fields=['- MULTIPLY_FACTOR : double = 0.8'],
    methods=['+ calculatePrice(actual : double) : double'])

# ── Column 3: RoomState interface ────────────────────────────────
b_roomState = draw_class(ax, C3, 20.8, W_WIDE, 'RoomState',
    stereotype='interface',
    methods=[
        '+ reserve(context : Room, r : Reservation)',
        '+ occupy(context : Room)',
        '+ release(context : Room)',
        '+ maintenance(context : Room)',
    ],
    hdr_color=IFACE_HDR, border_color=IFACE_BORDER)

b_available = draw_class(ax, C3, 15.5, W_WIDE, 'AvailableState',
    methods=[
        '+ reserve(context : Room, r : Reservation)',
        '+ occupy(context : Room)',
        '+ release(context : Room)',
        '+ maintenance(context : Room)',
    ])

b_reserved = draw_class(ax, C3, 12.0, W_WIDE, 'ReservedState',
    methods=[
        '+ reserve(context : Room, r : Reservation)',
        '+ occupy(context : Room)',
        '+ release(context : Room)',
        '+ maintenance(context : Room)',
    ])

b_occupied = draw_class(ax, C3, 8.5, W_WIDE, 'OccupiedState',
    methods=[
        '+ reserve(context : Room, r : Reservation)',
        '+ occupy(context : Room)',
        '+ release(context : Room)',
        '+ maintenance(context : Room)',
    ])

b_maintenance = draw_class(ax, C3, 5.0, W_WIDE, 'MaintenanceState',
    methods=[
        '+ reserve(context : Room, r : Reservation)',
        '+ occupy(context : Room)',
        '+ release(context : Room)',
        '+ maintenance(context : Room)',
    ])

# ═══════════════════════════════════════════════════════════════════
# RELATIONSHIPS
# ═══════════════════════════════════════════════════════════════════

# 1. HotelSystem --> ReservationProcessor  (uses / dependency)
arrow(ax, b_hotel['mid_bot'][0], b_hotel['mid_bot'][1],
          b_processor['mid_top'][0], b_processor['mid_top'][1],
      style='->', color=ARROW_COLOR, lw=1.5, dashed=True, label='«uses»',
      label_offset=(0.5, 0.1))

# 2. HotelSystem --> Room  (aggregates)
agg_arrow(ax, b_hotel['right'], b_hotel['cy'],
              b_room['left'], b_room['cy'],
              label='  rooms[ ]')

# 3. HotelSystem --> Guest  (aggregates)
agg_arrow(ax, b_hotel['left'], b_hotel['cy'],
              b_guest['right'], b_guest['cy'],
              label='guests[ ]')

# 4. ReservationProcessor --> Reservation  (manages)
arrow(ax, b_processor['left'], b_processor['cy'],
          b_reservation['right'], b_reservation['cy'],
      style='->', color=ARROW_COLOR, lw=1.4, dashed=True, label='  manages',
      label_offset=(0.0, 0.18))

# 5. Room --composition--> Calender
comp_arrow(ax, b_room['mid_bot'][0], b_room['mid_bot'][1],
               b_calender['mid_top'][0], b_calender['mid_top'][1],
               label='  calendar')

# 6. Room --> RoomState  (uses, state field)
arrow(ax, b_room['right'], b_room['cy'],
          b_roomState['left'], b_roomState['cy'],
      style='->', color=IFACE_BORDER, lw=1.4, dashed=True, label='state',
      label_offset=(0.0, 0.18))

# 7. Room --> PricingStrategy  (uses)
arrow(ax, b_room['mid_bot'][0] + 0.3, b_room['mid_bot'][1],
          b_pricing['mid_top'][0], b_pricing['mid_top'][1],
      style='->', color=IFACE_BORDER, lw=1.4, dashed=True, label='pricing',
      label_offset=(0.5, 0.1))

# 8. Room --> RoomType (uses)
agg_arrow(ax, b_room['left'], b_room['cy'],
              b_roomType['right'], b_roomType['cy'],
              label='type')

# 9. Guest --> Reservation  (aggregation)
arrow(ax, b_guest['mid_bot'][0], b_guest['mid_bot'][1],
          b_reservation['mid_top'][0], b_reservation['mid_top'][1],
      style='->', color=AGG_COLOR, lw=1.3, label='reservations[ ]',
      label_offset=(0.7, 0.1))

# 10. Reservation --> ReserveStatus
arrow(ax, b_reservation['mid_bot'][0], b_reservation['mid_bot'][1],
          b_resStatus['mid_top'][0], b_resStatus['mid_top'][1],
      style='->', color=ENUM_BORDER, lw=1.3, dashed=True, label='status',
      label_offset=(0.5, 0.1))

# 11. AvailableState --implements--> RoomState
impl_arrow(ax, b_available['mid_top'][0], b_available['mid_top'][1],
               b_roomState['mid_bot'][0], b_roomState['mid_bot'][1])

# 12. ReservedState --implements--> RoomState
#     go right then up
rx = b_reserved['right'] + 0.1
impl_arrow(ax, b_reserved['right'], b_reserved['cy'],
               b_roomState['mid_bot'][0], b_roomState['mid_bot'][1])

# 13. OccupiedState --implements--> RoomState
impl_arrow(ax, b_occupied['right'], b_occupied['cy'],
               b_roomState['mid_bot'][0], b_roomState['mid_bot'][1])

# 14. MaintenanceState --implements--> RoomState
impl_arrow(ax, b_maintenance['right'], b_maintenance['cy'],
               b_roomState['mid_bot'][0], b_roomState['mid_bot'][1])

# 15. OnSeasonStrategy --implements--> PricingStrategy
impl_arrow(ax, b_onSeason['mid_top'][0], b_onSeason['mid_top'][1],
               b_pricing['mid_bot'][0] - 0.3, b_pricing['mid_bot'][1])

# 16. OffSeasonStrategy --implements--> PricingStrategy
impl_arrow(ax, b_offSeason['mid_top'][0], b_offSeason['mid_top'][1],
               b_pricing['mid_bot'][0] + 0.3, b_pricing['mid_bot'][1])

# ── State transition annotation ────────────────────────────────────────
ax.text(19.8, 1.1,
        'State Transitions:\nAvailable ──reserve()──► Reserved ──occupy()──► Occupied\n'
        'Reserved  ──release()──► Available   (on cancel)\n'
        'Occupied  ──release()──► Available   (on checkout)\n'
        'Available / Occupied ──maintenance()──► Maintenance\n'
        'Maintenance ──release()──► Available',
        ha='left', va='bottom', fontsize=7.0,
        color='#8b949e', fontfamily='monospace',
        bbox=dict(boxstyle='round,pad=0.4', fc='#161b22', ec='#30363d', lw=1))

# ── Legend ────────────────────────────────────────────────────────────────
legend_x, legend_y = 0.3, 2.2
ax.text(legend_x, legend_y, 'Legend', fontsize=8, color=TEXT_WHITE, fontweight='bold')
items = [
    ('─────► ', ARROW_COLOR,    'Dependency / Uses'),
    ('- - -►', IMPL_COLOR,     'Implements / Realizes'),
    ('◆────►', COMP_COLOR,     'Composition'),
    ('- - - ', AGG_COLOR,      'Aggregation'),
]
for i, (sym, col, txt) in enumerate(items):
    ly = legend_y - 0.35 * (i+1)
    ax.text(legend_x, ly, sym, fontsize=7.5, color=col, fontfamily='monospace')
    ax.text(legend_x + 0.85, ly, txt, fontsize=7.5, color='#8b949e')

# ─── Save ────────────────────────────────────────────────────────────────────
output_path = r'c:\Users\satya\Desktop\lld\HotelMangement\HotelManagement_UML.png'
plt.tight_layout(pad=0.5)
plt.savefig(output_path, dpi=150, bbox_inches='tight',
            facecolor=BG, edgecolor='none')
print("UML diagram saved to HotelManagement_UML.png")
plt.close()
