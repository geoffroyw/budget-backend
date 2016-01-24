class Transaction < ApplicationRecord

  belongs_to :bank_account

  validates_presence_of :bank_account
  validates_presence_of :amount_cents
  validates_presence_of :amount_currency
  validates_presence_of :transaction_type
  validates_presence_of :date
  validates_presence_of :name

  monetize :amount_cents, with_model_currency: :amount_currency
end
